package com.testfeed.steps

import com.testfeed.ScenarioBuilder._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

trait LoginSteps {

  val loginUrl: String = s"$baseUrl/rest/user/login"
  val dslAuthTokenPattern = """"token":"(.+)","""
  val dslBasketIdPattern = """"bid":([\d]+),"""

  val login = http("Login")
    .post(loginUrl)
    .header("Content-Type", "application/json")
    .body(StringBody(
      """
        |{
        |  "email": "${email}",
        |  "password": "${password}"
        |}
      """.stripMargin))
    .check(regex(dslAuthTokenPattern).saveAs("authToken"))
    .check(regex(dslBasketIdPattern).saveAs("basketId"))
    .check(status lt 400)

}
