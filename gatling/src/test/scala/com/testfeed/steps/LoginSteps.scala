package com.testfeed.steps

import com.testfeed.ScenarioBuilder._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

trait LoginSteps {

  val loginUrl: String = s"$baseUrl/rest/user/login"
  val dslAuthTokenPattern = """"token":"(.+)","""
  val dslBasketIdPattern = """"bid":([\d]+),"""

}
