package com.thiefspin.util.json

import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json.{Format, JsError, JsSuccess, Json}
import scalaz._
import Scalaz._

import scala.util.Try

object JsonParser extends LazyLogging {

  type JsonParseResult[A] = JsonParseError \/ A

  /**
   * Parses string to type A.
   *
   * @param json
   * @tparam A
   * @return A
   */

  def apply[A: Format](json: String): JsonParseResult[A] = {
    Try(Json.parse(json)).toDisjunction match {
      case \/-(js) => js.validate[A] match {
        case JsSuccess(value, _) => \/-(value)
        case e: JsError => -\/(JsonParseError.error(e))
      }
      case -\/(error) => -\/(JsonParseError.error(error))
    }
  }
}
