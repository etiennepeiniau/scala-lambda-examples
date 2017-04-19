package commons

object ApiGatewayInternals {

  val CORS_HEADERS = Map(("Access-Control-Allow-Origin", "*"))

  case class AwsProxyRequest(httpMethod: String,
                             resource: String,
                             path: String,
                             requestContext: ApiGatewayRequestContext,
                             body: Option[String],
                             headers: Option[Map[String, String]],
                             queryStringParameters: Option[Map[String, String]],
                             pathParameters: Option[Map[String, String]],
                             stageVariables: Option[Map[String, String]])

  case class AwsProxyResponse(statusCode: Int,
                              headers: Map[String, String],
                              body: String)

  case class ApiGatewayRequestContext(apiId: String,
                                      requestId: String,
                                      accountId: String,
                                      httpMethod: String,
                                      resourceId: String,
                                      resourcePath: String,
                                      stage: String,
                                      identity: ApiGatewayRequestIdentity)

  case class ApiGatewayRequestIdentity(userAgent: String,
                                       sourceIp: String,
                                       apiKey: Option[String],
                                       accountId: Option[String],
                                       user: Option[String],
                                       userArn: Option[String],
                                       caller: Option[String],
                                       accessKey: Option[String])

}
