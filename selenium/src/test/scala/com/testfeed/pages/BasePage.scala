package com.testfeed.pages

import com.testfeed.config.TestConfig
import com.typesafe.config.Config
import org.openqa.selenium.{WebDriver, WebElement}
import org.scalatest.selenium.{Page, WebBrowser}

trait BasePage extends WebBrowser with Page
  with TestConfig {

  lazy val config: Config = juiceShopConfig
  def isDisplayed: Boolean
}