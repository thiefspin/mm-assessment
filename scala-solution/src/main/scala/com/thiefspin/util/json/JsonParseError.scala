package com.thiefspin.util.json

import play.api.libs.json.{JsError, Json, OFormat}

final case class JsonParseError(error: String)

object JsonParseError {

  implicit val format: OFormat[JsonParseError] = Json.format[JsonParseError]

  def error(jsError: JsError): JsonParseError = {
    JsonParseError(
      jsError.errors.map { error =>
        s"""
        Json parse error at path:
        ${error._1.toString}
        Errors:
        ${error._2.map(_.messages).mkString}
      """
      }.mkString
    )
  }

  def error(error: Throwable): JsonParseError = {
    JsonParseError(
      error.getMessage
    )
  }
}