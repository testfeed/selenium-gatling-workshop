package com.testfeed.pages

import org.openqa.selenium.{By, WebDriver}

case class ConfirmationPage(implicit driver: WebDriver) extends BasePage {

  override lazy val isDisplayed: Boolean = driver.getPageSource.contains("Order Confirmation")
  override val url: String = new String

  val bodyLocator: By = By.tagName("body")

  def getOrderConfirmation(): String = {
    WaitUtils.waitForElementToContainText(bodyLocator, "Order Confirmation")
    driver.getPageSource
  }
}
