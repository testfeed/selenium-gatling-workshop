package com.testfeed.steps

import com.testfeed.ScenarioBuilder._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

trait RegistrationSteps {

  val registrationUrl: String = s"$baseUrl/api/Users/"
  val securityQuestionsUrl: String = s"$baseUrl/api/SecurityQuestions/"
  val securityAnswersUrl: String = s"$baseUrl/api/SecurityAnswers/"
  val userIdPattern = """"id":([\d]+),"""

  val emailFeeder = Iterator.continually(Map("email" -> s"${Random.alphanumeric.take(10).mkString}@testing.com"))
  val passwordFeeder = Iterator.continually(Map("password" -> s"${Random.alphanumeric.take(15).mkString}"))

}
