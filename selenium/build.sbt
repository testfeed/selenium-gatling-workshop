name := "selenium-gatling-workshop"
organization := "com.testfeed"

scalaVersion := "2.11.11"

version := "0.1.0"

resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.scalatest"           %% "scalatest"                       % "3.0.3"  % "test"
libraryDependencies += "org.seleniumhq.selenium" % "selenium-java"                    % "3.14.0" % "test"
libraryDependencies += "net.lightbody.bmp"       % "browsermob-core"                  % "2.1.5"  % "test"
libraryDependencies += "org.pegdown"             % "pegdown"                          % "1.6.0"  % "test"
libraryDependencies += "com.typesafe"            % "config"                           % "1.3.4"  % "test"
libraryDependencies += "com.typesafe.play"       % "play-json_2.11"                   % "2.7.2"  % "test"

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/test-reports")
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports/html-report")
testOptions in Test += Tests.Argument("-oD")

parallelExecution in Test := false