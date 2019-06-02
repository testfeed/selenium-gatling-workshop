package com.testfeed.spec

import com.testfeed.pages._
import org.scalatest.concurrent.IntegrationPatience

import scala.util.Random

class CheckoutSpec extends BaseSpec with IntegrationPatience {

  scenario("should be able to checkout some juice when registering as a new customer") {

    Given("a user has registered")
    val email = s"${Random.alphanumeric.take(10).mkString}@testing.com"
    val password = s"${Random.alphanumeric.take(10).mkString}"
    val mmn = s"${Random.alphanumeric.take(10).mkString}"
    RegistrationPage().register(email, password, "Mother's maiden name?", mmn)
    LoginPage().isDisplayed

    And("subsequently logged in")
    LoginPage().login(email, password)
    HomePage().isDisplayed

    When("the user selects a juice for checkout")
    HomePage().addToBasket("Apple Juice (1000ml)")

    And("checks it out")
    go to BasketPage().url
    BasketPage().checkout()

    Then("the item is successfully purchased")
    ConfirmationPage().getOrderConfirmation() should include(email)
    go to BasketPage().url
    assert(BasketPage().basketIsEmpty())
  }
}