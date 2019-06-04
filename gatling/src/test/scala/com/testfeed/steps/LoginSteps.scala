package com.testfeed.steps

import com.testfeed.ScenarioBuilder._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

trait LoginSteps {

  val loginUrl: String = s"$baseUrl/rest/user/login"
  val authTokenPattern = """"token":([.]+),"""

  val login = http("Login")
    .post(login)
    .body(StringBody("{\"email\":\"${email}\",\"password\":\"K6tEPx9Usw\"}"))
    .check(regex(_ => authTokenPattern).saveAs("authToken"))
    .check(status lt 400)

}
