package com.testfeed

import com.testfeed.config.SimulationConfig
import com.testfeed.steps.{LoginSteps, RegistrationSteps}
import io.gatling.core.Predef._
import io.gatling.http.Predef.http

class CheckoutSimulation extends Simulation
  with SimulationConfig
  with LoginSteps
  with RegistrationSteps {

  val mainScn = scenario("Register and login")

  setUp(
    mainScn.inject(atOnceUsers(1))
  ).protocols(http.baseUrl(baseUrl))
}
