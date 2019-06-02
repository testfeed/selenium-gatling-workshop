package com.testfeed

import io.gatling.core.Predef._
import com.testfeed.config.SimulationConfig
import com.testfeed.steps.LoginSteps
import io.gatling.core.Predef.scenario
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.request.builder.HttpRequestBuilder

object ScenarioBuilder extends SimulationConfig
  with LoginSteps {

  def checkoutScenario(name: String, journeySteps: Seq[HttpRequestBuilder]): ScenarioBuilder = {

    journeySteps.foldLeft(
      scenario(name)
      .feed(userFeeder).exec(_.set("version", "0"))) { (steps, requestBuilder) =>
      steps.exec(requestBuilder).exitHereIfFailed
    }
  }
}
