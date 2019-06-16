package com.testfeed.steps

import io.gatling.core.Predef._
import io.gatling.http.Predef._

trait LoginSteps {

  val loginPath: String = "/rest/user/login"
  val dslAuthTokenPattern = """"token":"(.+)","""
  val dslBasketIdPattern = """"bid":([\d]+),"""

  val loginRequest = http("Login")
    .post(loginPath)
    .body(StringBody("test login"))
}
