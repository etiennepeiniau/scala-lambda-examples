package commons

import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._

object CommonResponses {

  implicit val errorEncoder: Encoder[Error] = Encoder.instance(a => ErrorResponse(a.fillInStackTrace().getMessage).asJson)

  sealed trait Response

  case class MessageResponse(message: String) extends Response

  case class ErrorResponse(message: String) extends Response

}
