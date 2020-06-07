package com.thiefspin.web.responses

import akka.http.scaladsl.model.{HttpEntity, StatusCode}
import play.api.libs.json.Json

final case class HttpError(statusCode: StatusCode) {

  def apply: HttpEntity.Strict = {
    HttpEntity.apply(
      Json.toJson(
        HttpErrorResponse(statusCode.value, statusCode.defaultMessage(), statusCode.intValue)
      ).toString
    )
  }
}
