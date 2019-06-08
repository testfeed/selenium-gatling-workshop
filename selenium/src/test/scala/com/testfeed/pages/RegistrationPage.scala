package com.testfeed.pages

import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{By, WebDriver}

case class RegistrationPage()(implicit driver: WebDriver) extends BasePage {

  override lazy val isDisplayed: Boolean = new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.id(emailFieldId))).isDisplayed
  override val url: String = s"${baseUrl("juice-shop")}/#/register"

  val emailFieldId: String = "emailControl"
  val passwordFieldId: String = "passwordControl"
  val repeatPasswordFieldId: String = "repeatPasswordControl"
  val securityQuestionFieldId: String = "mat-select-2"
  val securityAnswerFieldId: String = "securityAnswerControl"
  val registerButtonLocator: By = By.id("registerButton")

  def register(email: String, password: String, securityQuestion: String, securityAnswer: String): Unit = {

  }
}
