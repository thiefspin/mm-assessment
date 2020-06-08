package com.thiefspin.model

import play.api.libs.json.{Json, OFormat}

final case class UssdRequest(
  sessionId: String,
  msisdn: String,
  userEntry: Option[String]
)

object UssdRequest {
  implicit val format: OFormat[UssdRequest] = Json.format[UssdRequest]
}
