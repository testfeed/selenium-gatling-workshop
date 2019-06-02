package com.testfeed.pages

import org.openqa.selenium.{By, WebDriver}
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}

case class LoginPage(implicit driver: WebDriver) extends BasePage {

  override lazy val isDisplayed: Boolean = emailField(emailFieldId).isDisplayed
  override val url: String = s"${baseUrl("juice-shop")}/#/login"

  val emailFieldId: String = "email"
  val passwordFieldId: String = "password"
  val logInButtonLocator: By = By.id("loginButton")

  def login(email: String, password: String): Unit = {
    emailField(emailFieldId).value = email
    pwdField(passwordFieldId).value = password
    new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(logInButtonLocator)).click()
  }
}
