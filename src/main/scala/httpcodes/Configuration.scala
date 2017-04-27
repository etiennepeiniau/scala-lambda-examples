package httpcodes

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets.UTF_8

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import commons.ApiGatewayInternals.{AwsProxyResponse, CorsHeaders}
import io.circe.generic.auto._
import io.circe.syntax._
import org.apache.http.HttpStatus

// handler: httpcodes.Configuration
class Configuration extends RequestStreamHandler {

  case class Configuration(templateUrl: String, codes: List[Int])

  def handleRequest(in: InputStream, out: OutputStream, context: Context): Unit = {
    // Map all the HttpStatus into a configuration
    val codes = classOf[HttpStatus].getFields
      .map(f => f.getInt(f))
      // broken dog and cat images
      .filter(_ != 101)
      .filter(_ != 102)
      .filter(_ != 203)
      .filter(_ != 205)
      .filter(_ != 407)
      .filter(_ != 415)
      .filter(_ != 419)
      .filter(_ != 501)
      .filter(_ != 505)
      .toList
    // val templateUrl = "https://httpstatusdogs.com/img/_code_.jpg";
    val templateUrl = "https://http.cat/_code_"
    val configuration = Configuration(templateUrl, codes)

    // create and return an AwsProxyResponse
    val response = AwsProxyResponse(200, CorsHeaders, configuration.asJson.noSpaces)
    out.write(response.asJson.noSpaces.getBytes(UTF_8))
  }

}


