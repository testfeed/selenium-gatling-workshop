package com.testfeed.pages

import org.openqa.selenium.{By, WebDriver}

case class BasketPage(implicit driver: WebDriver) extends BasePage {

  override lazy val isDisplayed: Boolean = WaitUtils.waitForElementToContainText(pageHeadingLocator, "Your Basket")
  override val url: String = s"${baseUrl("juice-shop")}/#/basket"

  val pageHeadingLocator: By = By.className("heading")
  val checkoutButtonLocator: By = By.id("checkoutButton")

  def checkout(): Unit = {
    driver.navigate().to(url)
    WaitUtils.waitForElementToBeClickable(checkoutButtonLocator).click()
  }
}
