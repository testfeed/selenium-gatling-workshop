package com.testfeed

import com.testfeed.ScenarioJsonParser.loadDataFromFile
import com.testfeed.Utils.{makePathsDynamic, makePostPayloadsDynamic}
import com.testfeed.config.SimulationConfig
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.{http, status, _}

class CheckoutSimulation extends Simulation
  with SimulationConfig {

  val httpProtocol = http.baseUrl("http://localhost:3000")

  val mainScn = scenario("Checkout")
    .exec(buildJourneyRequests(loadDataFromFile("src/test/resources/data/checkoutJourney.json")))

    setUp(
      mainScn.inject(atOnceUsers(1))
    ).protocols(httpProtocol)
      .assertions(global.failedRequests.count.is(0))

  private def buildJourneyRequests(fragment: ScenarioFragment): Seq[ChainBuilder] = {
    fragment.scenarioRequests
      .map {
        createRequestBuilder(_)
      }
  }

  private def createRequestBuilder(shopRequest: JuiceShopRequest): ChainBuilder = {
    import shopRequest._

    val dynamicPath = makePathsDynamic(path)
    val pageUrl: String = s"$baseUrl$dynamicPath"

    val authTokenPattern = """"token":"(.+)","""
    val basketIdPattern = """bid":(\d+),"""
    val userIdPattern = """"isActive":true,"id":(\d+),"""
    val orderIdPattern = """orderConfirmation":"/ftp/(.+)"}"""

    val httpMethods = http(s"$method on $dynamicPath")

    exec((method match {
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
    }).check(status lt 400))
  }
}
