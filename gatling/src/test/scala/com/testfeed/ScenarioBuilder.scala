package com.testfeed

import com.testfeed.config.SimulationConfig
import com.testfeed.steps.{LoginSteps, RegistrationSteps}
import io.gatling.core.Predef.scenario
import io.gatling.core.structure.ScenarioBuilder

object ScenarioBuilder extends SimulationConfig
  with RegistrationSteps
  with LoginSteps {

  def checkoutScenario(name: String): ScenarioBuilder = {

    scenario(name)
      .exec(getSecurityQuestions).exitHereIfFailed
      .exec(register).feed(emailFeeder).feed(passwordFeeder).exitHereIfFailed
      .exec(storeSecurityAnswer).exitHereIfFailed
      .exec(login).exitHereIfFailed
  }
}
