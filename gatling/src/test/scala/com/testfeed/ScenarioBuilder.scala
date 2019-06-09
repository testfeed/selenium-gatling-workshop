package com.testfeed

import com.testfeed.config.SimulationConfig
import com.testfeed.steps.{LoginSteps, RegistrationSteps}
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

object ScenarioBuilder extends SimulationConfig
  with RegistrationSteps
  with LoginSteps {

  def checkoutScenario(name: String): ScenarioBuilder = {

    scenario(name).feed(emailFeeder).feed(passwordFeeder)
      .exec(getSecurityQuestions).exitHereIfFailed
      .exec(register).exitHereIfFailed
      .exec(storeSecurityAnswer).exitHereIfFailed
      .exec(login).exitHereIfFailed
  }
}
