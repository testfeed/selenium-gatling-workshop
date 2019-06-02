package com.testfeed

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object SimulationRunner {

  def main(args: Array[String]) {

    val props = new GatlingPropertiesBuilder
    props.simulationClass(classOf[CheckoutSimulation].getName)

    Gatling.fromMap(props.build)
  }
}
