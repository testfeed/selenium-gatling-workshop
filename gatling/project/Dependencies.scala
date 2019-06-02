import sbt._

object Dependencies {
  lazy val gatling = Seq(
    "io.gatling.highcharts" % "gatling-charts-highcharts",
    "io.gatling" % "gatling-test-framework"
  ).map(_ % "3.1.2" % Test)

  lazy val typeSafe = Seq(
    "com.typesafe" % "config" % "1.3.4",
    "com.typesafe.play" %% "play-json" % "2.7.3")
}
