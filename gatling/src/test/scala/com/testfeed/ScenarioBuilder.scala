package com.testfeed

import io.gatling.core.Predef._
import com.testfeed.config.SimulationConfig
import com.testfeed.steps.{LoginSteps, RegistrationSteps}
import io.gatling.core.Predef.scenario
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.request.builder.HttpRequestBuilder

import scala.util.Random

object ScenarioBuilder extends SimulationConfig
  with RegistrationSteps
  with LoginSteps {

  val randomCounterFeeder = Iterator.continually(Map("randomCounter" -> s"${Seq.fill(10)(Random.nextInt(9)).mkString("")}"))

  def checkoutScenario(name: String, journeySteps: Seq[HttpRequestBuilder]): ScenarioBuilder = {

    val registrationSteps = scenario(name)
      .feed(emailFeeder)
      .exec(getSecurityQuestions).exitHereIfFailed
      .exec(register).exitHereIfFailed
      .exec(storeSecurityAnswer).exitHereIfFailed

    val loginSteps = scenario(name)
      .feed(emailFeeder)
      .exec(login).exitHereIfFailed

    journeySteps.foldLeft(
      scenario(name).feed(randomCounterFeeder)
      .exec(registrationSteps).exec(loginSteps)) { (steps, requestBuilder) =>
      steps.exec(requestBuilder).exitHereIfFailed
    }
  }
}
