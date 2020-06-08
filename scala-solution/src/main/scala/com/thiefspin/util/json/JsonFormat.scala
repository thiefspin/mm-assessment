package com.thiefspin.util.json

import play.api.libs.json.{Format, Json}

trait JsonFormat[A] {
  entity: A =>

  /**
   * Converts class to Json object and returns string representation.
   *
   * @param format OFormat[A]
   * @return Json object as string representation
   */

  def asJsonString(implicit format: Format[A]): String = {
    Json.toJson(entity).toString
  }

}
