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
      .exec(registrationRequest)
      .exec(loginRequest)

  setUp(
    mainScn.inject(atOnceUsers(1))
  ).protocols(http.baseUrl(baseUrl).contentTypeHeader("application/json"))
}
