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



  val register = http("Registration")
    .post(registrationUrl)
    .header("Content-Type", "application/json")
    .body(StringBody(
      """
        |{
        |  "email": "${email}",
        |  "password": "qweqwe",
        |  "passwordRepeat": "qweqwe",
        |  "securityQuestion": {
        |    "id": 2,
        |    "question": "Mother's maiden name?",
        |    "createdAt": "2019-06-15T13:58:18.397Z",
        |    "updatedAt": "2019-06-15T13:58:18.397Z"
        |  },
        |  "securityAnswer": "tabitha"
        |}
      """.stripMargin
    ))
    .check(regex(userIdPattern).saveAs("userId"))
    .check(status lt 400)

  val storeSecurityAnswer = http("Security answer")
    .post(securityAnswersUrl)
    .header("Content-Type", "application/json")
    .body(StringBody(
      """
        |{
        |  "UserId": ${userId},
        |  "answer": "tabitha",
        |  "SecurityQuestionId": 2
        |}
      """.stripMargin
    ))
}
