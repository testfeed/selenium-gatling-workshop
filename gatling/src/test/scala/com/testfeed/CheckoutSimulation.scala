package com.testfeed

import com.testfeed.ScenarioBuilder._
import com.testfeed.ScenarioJsonParser.loadDataFromFile
import com.testfeed.Utils.{makePathsDynamic, makePostPayloadsDynamic}
import com.testfeed.config.SimulationConfig
import io.gatling.core.Predef._
import io.gatling.core.structure.{PopulationBuilder, ScenarioBuilder}
import io.gatling.http.Predef.{http, status, _}
import io.gatling.http.request.builder.HttpRequestBuilder

class CheckoutSimulation extends Simulation
  with SimulationConfig {

  private def withInjectedLoad(scenarioDefinitions: Seq[ScenarioDefinition]): Seq[PopulationBuilder] = scenarioDefinitions.map(scenarioDefinition => {

    val injectionSteps = List(
      rampUsersPerSec(minUsers).to(maxUsers).during(rampUpTime),
      constantUsersPerSec(maxUsers).during(constantRateTime),
      rampUsersPerSec(maxUsers).to(minUsers).during(rampDownTime)
    )
    scenarioDefinition.builder.inject(injectionSteps)
  })

  //for multiple journeys, the load acts as a distribution i.e. 25% or 0.25 of the times use journey A
  lazy val checkoutScenarioDefinitions = Seq(
    ScenarioConfig(s"src/test/resources/data/checkoutJourney.json", 1)
  ).map(config => ScenarioDefinition(checkoutScenario(config.fileName, buildJourneyRequests(loadDataFromFile(config.fileName))), config.load))

  if (runSingleUserJourney) {
    val injectedBuilders = checkoutScenarioDefinitions.map(scenarioDefinition => {
      scenarioDefinition.builder.inject(atOnceUsers(1))
    })
    setUp(injectedBuilders: _*).protocols(http.connectionHeader("close"))
      .assertions(global.failedRequests.count.is(0))
  } else {
    setUp(withInjectedLoad(checkoutScenarioDefinitions): _*).protocols(http.connectionHeader("close"))
      .assertions(global.failedRequests.percent.lt(1))
  }


  private def buildJourneyRequests(fragment: ScenarioFragment): Seq[HttpRequestBuilder] = {
    fragment.scenarioRequests
      .map {
        createRequestBuilder(_)
      }
  }

  private def createRequestBuilder(shopRequest: JuiceShopRequest): HttpRequestBuilder = {
    import shopRequest._

    val dynamicPath = makePathsDynamic(path)
    val pageUrl: String = s"$baseUrl$dynamicPath"

    val authTokenPattern = """"token":"(.+)","""
    val basketIdPattern = """bid":(\d+),"""
    val userIdPattern = """"isActive":true,"id":(\d+),"""
    val orderIdPattern = """orderConfirmation":"/ftp/(.+)"}"""

    val httpMethods = http(s"$method on $dynamicPath")
    (method match {
      case "GET" => httpMethods.get(pageUrl)
        .header("Authorization", "Bearer ${authToken}")
        .disableFollowRedirect
      case "POST" =>
        httpMethods
          .post(pageUrl)
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer ${authToken}")
          .check(regex(_ => userIdPattern).optional.saveAs("userId"))
          .check(regex(_ => orderIdPattern).optional.saveAs("orderId"))
          .check(regex(_ => authTokenPattern).optional.saveAs("authToken"))
          .check(regex(_ => basketIdPattern).optional.saveAs("basketId"))
          .body(StringBody(makePostPayloadsDynamic(body))).asJson
    }).check(status lt 400)
  }
}

case class ScenarioConfig(fileName: String, load: Double)

case class ScenarioDefinition(builder: ScenarioBuilder, load: Double) {
  def this(scenarioBuilder: ScenarioBuilder) {
    this(scenarioBuilder, 1.0)
  }
}
