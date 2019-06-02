package com.testfeed.steps

import com.testfeed.ScenarioBuilder._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

trait LoginSteps {

  val loginUrl: String = s"$baseUrl/#/login"

  val getLoginPage = http("Get login page")
    .get(loginUrl)
    .check(status lt 400)

  val doLogin = http("Login")
    .post(loginUrl)
    .formParam("email", "{email}")
    .check(status lt 400)

  val userFeeder = Iterator.continually(Map("email" -> s"${Seq.fill(10)(Random.nextInt(9)).mkString("")}@testing.com"))

}
