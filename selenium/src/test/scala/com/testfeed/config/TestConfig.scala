package com.testfeed.config

import com.typesafe.config.{Config, ConfigFactory}

trait TestConfig {

  val juiceShopConfig: Config = ConfigFactory.load()

  def baseUrl(frontend: String): String = {
    s"${host(frontend)}:${port(frontend)}"
  }

  def host(frontend: String): String = juiceShopConfig.getString(s"$frontend.host")

  def port(frontend: String): String = juiceShopConfig.getString(s"$frontend.port")
}
