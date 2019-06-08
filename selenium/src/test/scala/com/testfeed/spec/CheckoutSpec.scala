package com.testfeed.spec

import org.scalatest.concurrent.IntegrationPatience

class CheckoutSpec extends BaseSpec with IntegrationPatience {

  scenario("should be able to checkout some juice when registering as a new customer") {

    Given("a user has registered and logged in")

    And("subsequently logged in")

    When("the user selects a juice for checkout")

    And("checks it out")

    Then("the item is successfully purchased")

  }
}