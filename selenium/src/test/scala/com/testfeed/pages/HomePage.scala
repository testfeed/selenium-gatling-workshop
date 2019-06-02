package com.testfeed.pages

import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{By, WebDriver}

import scala.collection.JavaConversions._

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
    val rowContainingProduct = driver.findElement(productTableLocator).findElements(productRowsLocator)
      .filter(_.findElements(columnCellLocator).map(_.getText).contains(product)).head

    rowContainingProduct.findElement(basketLocator).click()
    assert(new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(confirmationMessageLocator)).getText.contains(product))
  }
}
