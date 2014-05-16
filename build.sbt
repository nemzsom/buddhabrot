name := "buddhabrot"

version := "0.1"

scalaVersion := "2.11.0"

resolvers += "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.11" % "2.3.2",
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
  "org.scalatest" % "scalatest_2.11" % "2.1.6" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.4" % "test"
)
