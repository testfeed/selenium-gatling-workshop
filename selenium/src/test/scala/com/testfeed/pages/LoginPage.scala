package com.testfeed.pages

import org.openqa.selenium.{By, WebDriver}

case class LoginPage(implicit driver: WebDriver) extends BasePage {

  override lazy val isDisplayed: Boolean = WaitUtils.waitForElementToContainText(logInButtonLocator, "Log in")
  override val url: String = s"${baseUrl("juice-shop")}/#/login"

  val emailFieldId: String = "email"
  val passwordFieldId: String = "password"
  val logInButtonLocator: By = By.id("loginButton")

  def login(email: String, password: String): Unit = {
    driver.navigate().to(url)
    driver.findElement(By.id(emailFieldId)).sendKeys(email)
    driver.findElement(By.id(passwordFieldId)).sendKeys(password)
    driver.findElement(logInButtonLocator).click()
    HomePage().isDisplayed
  }
}
