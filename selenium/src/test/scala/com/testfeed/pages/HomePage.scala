package com.testfeed.pages

import org.openqa.selenium.{By, WebDriver}

case class HomePage(implicit driver: WebDriver) extends BasePage {

  override lazy val isDisplayed: Boolean = driver.findElement(headerCellLocator).isDisplayed
  override val url: String = s"${baseUrl("juice-shop")}/#/"

  val headerCellLocator: By = By.cssSelector("mat-header-row > mat-header-cell")
  val productTableLocator: By = By.tagName("mat-table")
  val productRowsLocator: By = By.tagName("mat-row")
  val columnCellLocator: By = By.tagName("mat-cell")
  val basketLocator: By = By.cssSelector("[data-icon='cart-plus']")
  val confirmationMessageLocator: By = By.className("confirmation")

  def addToBasket(product: String): Unit = {

  }
}
