package com.testfeed.spec

import com.testfeed.config.TestConfig
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxOptions}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.selenium.WebBrowser

import scala.util.Try


trait BaseSpec extends FeatureSpec
  with GivenWhenThen
  with WebBrowser
  with BeforeAndAfterAll
  with BeforeAndAfterEachTestData
  with TestConfig
  with ScalaFutures
  with Matchers {

  implicit lazy val driver: WebDriver = {
    val options: FirefoxOptions = new FirefoxOptions()
    val driver = new FirefoxDriver(options)
    driver
  }

  override def beforeEach(testData: TestData): Unit = {
    deleteAllCookies()
  }

  val urlExclusions = Seq("admin", "whoami", "Challenges", "languages", "json", "socket")

  override def afterEach(testData: TestData): Unit = {

  }

  override def beforeAll() {
    super.beforeAll()
    sys.addShutdownHook {
      Try(driver.quit())
    }
  }

  override def afterAll() {
    Try(driver.quit())
  }

  override def withFixture(test: NoArgTest): Outcome = {
    var fixture = super.withFixture(test)
    if (!fixture.isSucceeded) capture to test.name
    fixture
  }
}