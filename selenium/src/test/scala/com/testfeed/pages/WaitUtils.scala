package com.testfeed.pages

import org.openqa.selenium.{By, WebDriver, WebElement}
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}

object WaitUtils {

  def waitForElementToBeClickable(locator: By)(implicit driver: WebDriver): WebElement = {
    new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(locator))
  }

  def waitForElementToContainText(locator: By, text: String)(implicit driver: WebDriver): Boolean = {
    new WebDriverWait(driver, 5).until(ExpectedConditions.textToBePresentInElementLocated(locator, text))
  }

  def waitForElementToBeVisible(locator: By)(implicit driver: WebDriver): WebElement = {
    new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(locator))
  }

}
