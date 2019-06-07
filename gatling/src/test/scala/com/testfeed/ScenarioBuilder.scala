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

  def checkoutScenario(name: String, journeySteps: Seq[HttpRequestBuilder]): ScenarioBuilder = {

    val registrationSteps = scenario(name)
      .exec(getSecurityQuestions).exitHereIfFailed
      .exec(register).exitHereIfFailed
      .exec(storeSecurityAnswer).exitHereIfFailed

    val loginSteps = scenario(name)
      .exec(login).exitHereIfFailed

    journeySteps.foldLeft(
      scenario(name).feed(emailFeeder).feed(passwordFeeder).feed(dummyAuthFeeder)) { (steps, requestBuilder) =>
      steps.exec(requestBuilder).exitHereIfFailed
    }
  }
}
