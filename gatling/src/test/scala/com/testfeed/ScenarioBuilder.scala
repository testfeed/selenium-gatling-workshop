package com.testfeed

import com.testfeed.config.SimulationConfig
import com.testfeed.steps.{LoginSteps, RegistrationSteps}
import io.gatling.core.Predef.scenario
import io.gatling.core.structure.ScenarioBuilder

object ScenarioBuilder extends SimulationConfig
  with RegistrationSteps
  with LoginSteps {

  def checkoutScenario(name: String): ScenarioBuilder = {

    val registrationSteps = scenario(name)
      .exec(getSecurityQuestions).exitHereIfFailed
      .exec(register).exitHereIfFailed
      .exec(storeSecurityAnswer).exitHereIfFailed

    val loginSteps = scenario(name)
      .exec(login).exitHereIfFailed

    scenario(name).exec(registrationSteps, loginSteps).exitHereIfFailed
  }
}
