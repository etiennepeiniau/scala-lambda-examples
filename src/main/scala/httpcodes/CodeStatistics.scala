package httpcodes

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets.UTF_8

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import commons.ApiGatewayInternals.{AwsProxyResponse, CORS_HEADERS}
import io.circe.generic.auto._
import io.circe.syntax._

import scala.collection.JavaConverters._

// handler: httpcodes.CodeStatistics
class CodeStatistics extends RequestStreamHandler {

  val dynamoDB: DynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient)

  def handleRequest(in: InputStream, out: OutputStream, context: Context): Unit = {
    // get all the table elements
    val table = dynamoDB.getTable("HttpCodeStatistics")
    val statistics = table.scan().iterator().asScala
      .map(item => (item.getInt("code"), item.getInt("occurences")))
      .toMap

    // create and return an AwsProxyResponse
    val response = AwsProxyResponse(200, CORS_HEADERS, statistics.asJson.noSpaces)
    out.write(response.asJson.noSpaces.getBytes(UTF_8))
  }

}


