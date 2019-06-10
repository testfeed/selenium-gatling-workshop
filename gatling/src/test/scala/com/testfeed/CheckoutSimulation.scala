package com.testfeed

import com.testfeed.ScenarioBuilder._
import com.testfeed.config.SimulationConfig
import io.gatling.core.Predef._
import io.gatling.core.structure.PopulationBuilder
import io.gatling.http.Predef.http

class CheckoutSimulation extends Simulation
  with SimulationConfig {

  if (runSingleUserJourney) {
    val injectedBuilders = Seq(checkoutScenario("checkout juice").inject(atOnceUsers(1)))

    setUp(injectedBuilders: _*).protocols(http)
      .assertions(global.failedRequests.count.is(0))

  } else {

    setUp(withInjectedLoad(): _*).protocols(http)
      .assertions(global.failedRequests.percent.lt(1))
  }

  private def withInjectedLoad(): Seq[PopulationBuilder] = {

    val injectionSteps = List(
      rampUsersPerSec(minUsers).to(maxUsers).during(rampUpTime),
      constantUsersPerSec(maxUsers).during(constantRateTime),
      rampUsersPerSec(maxUsers).to(minUsers).during(rampDownTime)
    )
    Seq(checkoutScenario("checkout juice").inject(injectionSteps))
  }
}
