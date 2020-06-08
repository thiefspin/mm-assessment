package com.thiefspin.web.health

import com.thiefspin.util.json.JsonFormat
import play.api.libs.json.{Json, OFormat}

final case class HealthResponse(
  status: String
) extends JsonFormat[HealthResponse]

object HealthResponse {
  implicit val format: OFormat[HealthResponse] = Json.format[HealthResponse]
}
