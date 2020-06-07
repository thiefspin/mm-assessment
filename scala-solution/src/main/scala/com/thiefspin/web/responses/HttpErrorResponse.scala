package com.thiefspin.web.responses

import play.api.libs.json.{Json, OFormat}

final case class HttpErrorResponse(name: String, description: String, code: Long)

object HttpErrorResponse {
  implicit val format: OFormat[HttpErrorResponse] = Json.format[HttpErrorResponse]
}
