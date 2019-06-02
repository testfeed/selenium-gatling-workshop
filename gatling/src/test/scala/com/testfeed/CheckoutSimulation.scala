package com.testfeed

import com.testfeed.ScenarioBuilder._
import com.testfeed.ScenarioJsonParser.loadDataFromFile
import com.testfeed.config.SimulationConfig
import io.gatling.core.Predef._
import io.gatling.core.session.Expression
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

  if(runSingleUserJourney) {
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
      .map {createRequestBuilder(_)}
  }

  private def createRequestBuilder(shopRequest: JuiceShopRequest): HttpRequestBuilder = {
    import shopRequest._
    val pageUrl: Expression[String] = s"$baseUrl${shopRequest.url}"

    val title: String = s"${shopRequest.method} on ${shopRequest.url}"
    val httpMethods = http(title)
    (shopRequest.method match {
      case "GET" => httpMethods.get(pageUrl)
        .disableFollowRedirect
      case "POST" =>
        body.get.foldLeft(httpMethods.post(pageUrl)
          .disableFollowRedirect) {
          (e, shopRequest) => e.body(StringBody(shopRequest.toString)).asJson
        }
    }).check(status lt 400)
  }
}

case class ScenarioConfig(fileName: String, load: Double)

case class ScenarioDefinition(builder: ScenarioBuilder, load: Double) {
  def this(scenarioBuilder: ScenarioBuilder) {
    this(scenarioBuilder, 1.0)
  }
}
