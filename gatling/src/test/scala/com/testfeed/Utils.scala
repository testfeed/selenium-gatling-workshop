package com.testfeed

import play.api.libs.json.JsString

import scala.util.Random

object Utils {

  val emailFeeder = Iterator.continually(Map("email" -> s"${Random.alphanumeric.take(10).mkString}@testing.com"))
  val passwordFeeder = Iterator.continually(Map("password" -> s"${Random.alphanumeric.take(15).mkString}"))
  val dummyAuthFeeder = Iterator.fill(1)(Map("authToken" -> ""))

  def makePathsDynamic(path: String): String = {
    val BasketPathPattern = """.+/basket/(\d+)[/checkout"]?.*""".r
    val OrderIdPattern = ".*ftp/(.+)".r

    path match {
      case BasketPathPattern(basketId) =>
        path.replace(basketId, "${basketId}")
      case OrderIdPattern(orderId) =>
        path.replace(orderId, "${orderId}")
      case _ => path
    }
  }


  def makePostPayloadsDynamic(body: Option[JsString]): String = {
    val EmailRegistrationPattern = """.*email":"(\w+@testing.com)","password":"(\w+)","passwordRepeat":"(\w+)",.*""".r
    val LoginEmailPattern = """.*email":"(\w+@testing.com)","password":"(\w+)".*""".r
    val UserIdJsonPattern = """.*UserId":(\d+),.*""".r
    val BasketIdPattern = """.+BasketId\":\"(\d+).+""".r


    body.get.value match {
      case EmailRegistrationPattern(email, password, passwordRepeat) =>
        body.get.value.replace(password, "${password}")
          .replace(email, "${email}")
          .replace(passwordRepeat, "${password}")
      case LoginEmailPattern(email, password) =>
        body.get.value.replace(email, "${email}")
          .replace(password, "${password}")
      case BasketIdPattern(basketId) => body.get.value.replace(basketId, "${basketId}")
      case UserIdJsonPattern(userId) => body.get.value.replace(userId, "${userId}")
      case _ => body.get.value
    }
  }
}
