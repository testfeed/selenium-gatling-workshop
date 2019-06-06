package com.testfeed.spec

import java.net.{InetAddress, URL}

import com.testfeed.config.TestConfig
import net.lightbody.bmp.client.ClientUtil
import net.lightbody.bmp.core.har.{HarEntry, HarRequest}
import net.lightbody.bmp.proxy.CaptureType
import net.lightbody.bmp.{BrowserMobProxy, BrowserMobProxyServer}
import org.openqa.selenium.firefox.{FirefoxDriver, FirefoxOptions}
import org.openqa.selenium.{Proxy, WebDriver}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.selenium.WebBrowser
import play.api.libs.json.{JsArray, JsObject, JsString}

import scala.collection.JavaConversions._
import scala.util.Try


trait BaseSpec extends FeatureSpec
  with GivenWhenThen
  with WebBrowser
  with BeforeAndAfterAll
  with BeforeAndAfterEachTestData
  with TestConfig
  with ScalaFutures
  with Matchers {

  val proxy: BrowserMobProxy = new BrowserMobProxyServer()

  implicit lazy val driver: WebDriver = {
    proxy.start()
    val seleniumProxy: Proxy = ClientUtil.createSeleniumProxy(proxy, InetAddress.getByName("localhost"))
    val options: FirefoxOptions = new FirefoxOptions()
    options.setProxy(seleniumProxy)
    options.addPreference("network.proxy.allow_hijacking_localhost", true)
    val driver = new FirefoxDriver(options)
    proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.REQUEST_HEADERS, CaptureType.RESPONSE_HEADERS)
    driver
  }

  override def beforeEach(testData: TestData): Unit = {
    deleteAllCookies()
    proxy.newHar(testData.name)
  }

  val urlExclusions = Seq("admin", "whoami", "Challenges", "languages", "json", "socket")

  override def afterEach(testData: TestData): Unit = {
    val url = baseUrl("juice-shop")
    val headerRegex = "text/html|text/plain".r
    println(JsObject(Seq("scenarioRequests" -> JsArray(proxy.getHar.getLog.getEntries
      .filter(_.getRequest.getUrl.contains(url))
      .filter(_.getRequest.getHeaders.exists(header => headerRegex.findFirstMatchIn(header.getValue).isDefined))
      .filterNot(harEntry => {
        val url: String = harEntry.getRequest.getUrl
        urlExclusions.exists(new URL(url).getPath.contains(_))
      })
      .map { entry: HarEntry =>
        val request: HarRequest = entry.getRequest
        val url: String = request.getUrl
        val path = url.substring(baseUrl("juice-shop").length)
        JsObject(Seq(
          "method" -> JsString(request.getMethod),
          "elapsed" -> JsString(entry.getTime.toString),
          "path" -> JsString(path),
          "headers" -> JsObject(request.getHeaders
            .filterNot(_.getName.contains("Cookie"))
            .map(header => header.getName -> JsString(header.getValue)))
        ).++(if (request.getMethod == "POST") {
          Seq("body" -> JsString(request.getPostData.getText))
        } else Seq.empty))
      }))))
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