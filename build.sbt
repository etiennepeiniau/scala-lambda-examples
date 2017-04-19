import sun.security.tools.PathList

name := "scala-lambda-examples"
version := "1.0"

scalaVersion := "2.12.1"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-core"   % "1.1.0" % Provided,
  "com.amazonaws" % "aws-lambda-java-log4j" % "1.0.0" % Provided,
  "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.105"
)

val circeVersion = "0.7.0"
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
