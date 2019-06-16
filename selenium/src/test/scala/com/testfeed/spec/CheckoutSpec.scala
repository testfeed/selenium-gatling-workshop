package com.testfeed.spec

import com.testfeed.pages.{BasketPage, ConfirmationPage, HomePage, LoginPage, RegistrationPage}
import org.scalatest.concurrent.IntegrationPatience

import scala.util.Random

class CheckoutSpec extends BaseSpec with IntegrationPatience {

  scenario("should be able to checkout some juice when registering as a new customer") {
    val email = s"${Random.alphanumeric.take(10).mkString}@testing.com"
    val password = Random.alphanumeric.take(20).mkString

    Given("a user has registered and logged in")
    RegistrationPage().register(email, password, "Mother's maiden name", "Tabitha")

    And("subsequently logged in")
    LoginPage().login(email, password)

    When("the user selects a juice for checkout")
    HomePage().addToBasket("Apple Juice (1000ml)")

    And("checks it out")
    BasketPage().checkout()

    Then("the item is successfully purchased")
    ConfirmationPage().getOrderConfirmation() should include(email)
  }
}