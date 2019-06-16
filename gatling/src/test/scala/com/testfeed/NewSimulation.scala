package com.testfeed

import com.testfeed.config.SimulationConfig
import com.testfeed.steps.RegistrationSteps
import io.gatling.core.Predef._
import io.gatling.http.Predef.http

class NewSimulation extends Simulation
  with SimulationConfig
  with RegistrationSteps {

  val scn = scenario("register and login").feed(emailFeeder)
    .exec(register)
    .exec(storeSecurityAnswer)

  val httpProtocol = http.baseUrl("http://localhost:3000")

  val injectionSteps = List(
    rampUsersPerSec(minUsers).to(maxUsers).during(rampUpTime),
    constantUsersPerSec(maxUsers).during(constantRateTime),
    rampUsersPerSec(maxUsers).to(minUsers).during(rampDownTime)
  )


  setUp(
    scn.inject(injectionSteps)
  ).protocols(httpProtocol)

}
