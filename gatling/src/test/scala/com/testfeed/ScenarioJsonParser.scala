package com.testfeed

import scala.io.Source
import play.api.libs.json.Json.{fromJson, parse}
import play.api.libs.json.{JsError, JsString, JsSuccess, Json, Reads}

case class ScenarioFragment(scenarioRequests: Seq[JuiceShopRequest])
case class JuiceShopRequest(method: String, url: String, body: Option[JsString])


object ScenarioJsonParser {

  def loadDataFromFile(fileName: String): ScenarioFragment = {
    implicit val requestReads: Reads[JuiceShopRequest] = Json.reads[JuiceShopRequest]
    implicit val dataReads: Reads[ScenarioFragment] = Json.reads[ScenarioFragment]

    val file = Source.fromFile(fileName)
    val json = file.iter.mkString

    val dataMaybe = fromJson[ScenarioFragment](parse(json))

    dataMaybe match {
      case JsError(errors) => throw new RuntimeException(s"Encountered an error parsing json file $fileName: " + errors)
      case JsSuccess(d, p) => d
    }
  }

}
