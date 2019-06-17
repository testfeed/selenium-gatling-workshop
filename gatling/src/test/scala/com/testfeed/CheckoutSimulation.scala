package com.testfeed

import com.testfeed.ScenarioJsonParser.loadDataFromFile
import com.testfeed.Utils._
import com.testfeed.config.SimulationConfig
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.{http, _}

class CheckoutSimulation extends Simulation
  with SimulationConfig {

  val httpProtocol = http.baseUrl(baseUrl)
    .header("Content-Type", "application/json")
    .header("Authorization", "Bearer ${authToken}")


  val mainScn = scenario("Checkout")
    .feed(emailFeeder)
    .feed(passwordFeeder)
    .feed(dummyAuthFeeder)
    .exec(buildJourneyRequests(loadDataFromFile("src/test/resources/data/checkoutJourney.json")))

  val injectionSteps = List(
    rampUsersPerSec(minUsers).to(maxUsers).during(rampUpTime),
    constantUsersPerSec(maxUsers).during(constantRateTime),
    rampUsersPerSec(maxUsers).to(minUsers).during(rampDownTime)
  )

  setUp(
    mainScn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

  private def buildJourneyRequests(fragment: ScenarioFragment): Seq[ChainBuilder] = {
    fragment.scenarioRequests.map(createRequestBuilder)
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

    exec(method match {
      case "GET" => httpMethods.get(pageUrl)
      case "POST" =>
        httpMethods
          .post(pageUrl)
          .check(regex(userIdPattern).optional.saveAs("userId"))
          .check(regex(orderIdPattern).optional.saveAs("orderId"))
          .check(regex(authTokenPattern).optional.saveAs("authToken"))
          .check(regex(basketIdPattern).optional.saveAs("basketId"))
          .body(StringBody(makePostPayloadsDynamic(body))).asJson
    })
  }
}
