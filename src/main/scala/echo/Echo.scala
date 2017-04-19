package echo

import java.io.{InputStream, OutputStream}
import java.nio.charset.StandardCharsets.UTF_8

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}

// handler: echo.Echo
class Echo extends RequestStreamHandler {

  def handleRequest(in: InputStream, out: OutputStream, context: Context): Unit = {
    val payload = scala.io.Source.fromInputStream(in).mkString("")
    println(payload)
    out.write(payload.getBytes(UTF_8))
  }

}
