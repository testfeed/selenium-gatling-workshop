package com.testfeed.pages

import org.openqa.selenium.{By, WebDriver}
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}

case class ConfirmationPage(implicit driver: WebDriver) extends BasePage {

  override lazy val isDisplayed: Boolean = driver.getPageSource.contains("Order Confirmation")
  override val url: String = new String

  val bodyLocator: By = By.tagName("body")

  def getOrderConfirmation(): String = {
    new WebDriverWait(driver, 5).until(ExpectedConditions.textToBePresentInElementLocated(bodyLocator, "Order Confirmation"))
    driver.getPageSource
  }
}
