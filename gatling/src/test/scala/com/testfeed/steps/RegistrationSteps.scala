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

  val getSecurityQuestions = http("Get security questions")
    .get(securityQuestionsUrl)
    .check(status lt 400)

  val register = http("Registration")
    .post(registrationUrl)
    .body(StringBody("{\"email\":\"${email}\",\"password\":\"K6tEPx9Usw\",\"passwordRepeat\":\"K6tEPx9Usw\",\"securityQuestion\":{\"id\":2,\"question\":\"Mother's maiden name?\",\"createdAt\":\"2019-06-03T19:54:37.113Z\",\"updatedAt\":\"2019-06-03T19:54:37.113Z\"},\"securityAnswer\":\"oo4PGZpyzR\"}"))
    .check(regex(_ => userIdPattern).saveAs("userId"))
    .check(status lt 400)

  val storeSecurityAnswer = http("Send security answer")
    .post(securityAnswersUrl)
    .body(StringBody("{\"UserId\":${userId},\"answer\":\"oo4PGZpyzR\",\"SecurityQuestionId\":2}"))
    .check(status lt 400)
}