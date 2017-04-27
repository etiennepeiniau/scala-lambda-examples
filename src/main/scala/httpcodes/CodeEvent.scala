package httpcodes

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets.UTF_8

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec
import com.amazonaws.services.dynamodbv2.document.utils.{NameMap, ValueMap}
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item}
import com.amazonaws.services.dynamodbv2.model.ReturnValue
import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import commons.ApiGatewayInternals.CorsHeaders
import commons.ApiGatewayInternals.{AwsProxyRequest, AwsProxyResponse}
import commons.CommonResponses._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import scala.io.Source

// handler: httpcodes.CodeEvent
class CodeEvent extends RequestStreamHandler {

  val dynamoDB: DynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient)

  def handleRequest(in: InputStream, out: OutputStream, context: Context): Unit = {
    val payload = Source.fromInputStream(in).mkString

    val reply = decode[AwsProxyRequest](payload) match {
      case Left(error) => parsingError(error)
      case Right(request) => recordRequest(request)
    }

    out.write(reply.asJson.noSpaces.getBytes(UTF_8))
  }

  def recordRequest(request: AwsProxyRequest): AwsProxyResponse = {
    request.pathParameters.flatMap(_.get("code")).map(_.toInt).map(code => {
      // record put into dynamoDB
      val table = dynamoDB.getTable("HttpCodeStatistics")
      val item = table.getItem("code", code)
      if (item == null) {
        // create the code if it does not exists with occurences set to 1
        val newItem = new Item().withPrimaryKey("code", code).withInt("occurences", 1)
        table.putItem(newItem)
        AwsProxyResponse(200, CorsHeaders, newItem.toJSON)
      }
      else {
        // increment the occurens if the code exists
        val updateItemSpec = new UpdateItemSpec()
          .withPrimaryKey("code", code)
          .withReturnValues(ReturnValue.ALL_NEW)
          .withUpdateExpression("add #o :val1")
          .withNameMap(new NameMap().`with`("#o", "occurences"))
          .withValueMap(new ValueMap().withNumber(":val1", 1))
        val updatedItem = table.updateItem(updateItemSpec).getItem
        AwsProxyResponse(200, CorsHeaders, updatedItem.toJSON)
      }
    }).getOrElse(parameterError)
  }

  def parsingError(error: Error): AwsProxyResponse = {
    AwsProxyResponse(500, CorsHeaders, error.asJson.noSpaces)
  }

  def parameterError: AwsProxyResponse = {
    AwsProxyResponse(400, CorsHeaders, ErrorResponse("Code path parameter is missing").asJson.noSpaces)
  }

}


