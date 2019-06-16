package com.testfeed.pages

import org.openqa.selenium.{By, WebDriver}

case class RegistrationPage()(implicit driver: WebDriver) extends BasePage {

  override lazy val isDisplayed: Boolean = WaitUtils.waitForElementToContainText(registerButtonLocator, "Register")
  override val url: String = s"${baseUrl("juice-shop")}/#/register"

  val emailFieldId: String = "emailControl"
  val passwordFieldId: String = "passwordControl"
  val repeatPasswordFieldId: String = "repeatPasswordControl"
  val securityQuestionFieldId: String = "mat-select-2"
  val securityAnswerFieldId: String = "securityAnswerControl"
  val registerButtonLocator: By = By.id("registerButton")

  def register(email: String, password: String, securityQuestion: String, securityAnswer: String): Unit = {
    driver.navigate().to(url)
    driver.findElement(By.id(emailFieldId)).sendKeys(email)
    driver.findElement(By.id(passwordFieldId)).sendKeys(password)
    driver.findElement(By.id(repeatPasswordFieldId)).sendKeys(password)
    driver.findElement(By.id(securityQuestionFieldId))sendKeys(securityQuestion)
    driver.findElement(By.id(securityAnswerFieldId)).sendKeys(securityAnswer)
    driver.findElement(registerButtonLocator).click()
    WaitUtils.waitForElementToBeClickable(registerButtonLocator).click()
    LoginPage().isDisplayed
  }
}
