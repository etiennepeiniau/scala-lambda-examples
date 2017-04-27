package apigateway

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets.UTF_8

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import commons.ApiGatewayInternals.{AwsProxyRequest, AwsProxyResponse}
import commons.CommonResponses._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import scala.io.Source

// handler: apigateway.ApiGatewayHelloProxy
class ApiGatewayHelloProxy extends RequestStreamHandler {

  def handleRequest(in: InputStream, out: OutputStream, context: Context): Unit = {
    val payload = Source.fromInputStream(in).mkString

    val reply = decode[AwsProxyRequest](payload) match {
      case Left(error) => AwsProxyResponse(
        500,
        Map.empty,
        error.asJson.noSpaces)

      case Right(request) => AwsProxyResponse(
        200,
        Map.empty,
        s"Hello ${request.pathParameters.flatMap(_.get("firstname")).getOrElse("Unknown")}, your IP is ${request.requestContext.identity.sourceIp}");
    }

    out.write(reply.asJson.noSpaces.getBytes(UTF_8))
  }

}


