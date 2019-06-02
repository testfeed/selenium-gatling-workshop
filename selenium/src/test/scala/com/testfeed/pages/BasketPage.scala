package com.testfeed.pages

import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{By, WebDriver}

case class BasketPage(implicit driver: WebDriver) extends BasePage {

  override lazy val isDisplayed: Boolean = driver.findElement(pageHeadingLocator).getText.contains("Your Basket")
  override val url: String = s"${baseUrl("juice-shop")}/#/basket"

  val pageHeadingLocator: By = By.className("heading")
  val checkoutButtonLocator: By = By.id("checkoutButton")

  def checkout(): Unit = {
    new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(checkoutButtonLocator)).click()
    assert(driver.getCurrentUrl.contains("pdf"))
  }

  def basketIsEmpty(): Boolean = {
    !driver.findElement(checkoutButtonLocator).isEnabled
  }
}
