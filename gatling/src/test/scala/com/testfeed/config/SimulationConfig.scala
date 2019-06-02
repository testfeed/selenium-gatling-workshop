package com.testfeed.config

import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._

trait SimulationConfig {

  val config: Config = ConfigFactory.load()
  val baseUrl: String = config.getString("baseUrl")

  val runSingleUserJourney: Boolean = config.getBoolean("perftest.runSingleUserJourney")

  val rampUpTime: FiniteDuration = config.getString("perftest.rampUpTime").toInt minutes
  val constantRateTime: FiniteDuration = config.getString("perftest.constantRateTime").toInt minutes
  val rampDownTime: FiniteDuration = config.getString("perftest.rampDownTime").toInt minutes

  val minUsers: Double = config.getString("perftest.minUsers").toDouble
  val maxUsers: Double = config.getString("perftest.maxUsers").toDouble

}
