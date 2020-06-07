package com.thiefspin.model

import com.thiefspin.util.json.JsonFormat
import play.api.libs.json.{Json, OFormat}

final case class UssdResponse(
  sessionId: String,
  message: String
) extends JsonFormat[UssdResponse]

object UssdResponse {
  implicit val format: OFormat[UssdResponse] = Json.format[UssdResponse]
}
