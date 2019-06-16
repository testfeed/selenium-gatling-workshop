package com.testfeed.steps

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

trait RegistrationSteps {

  val registrationPath: String = "/api/Users/"
  val securityQuestionsUrl: String = "api/SecurityQuestions/"
  val securityAnswersUrl: String = "/api/SecurityAnswers/"
  val userIdPattern = """"id":([\d]+),"""

  val emailFeeder = Iterator.continually(Map("email" -> s"${Random.alphanumeric.take(10).mkString}@testing.com"))
  val passwordFeeder = Iterator.continually(Map("password" -> s"${Random.alphanumeric.take(15).mkString}"))

  val registrationRequest = http("Registration")
    .post(registrationPath)
    .body(StringBody("test registration"))

}
