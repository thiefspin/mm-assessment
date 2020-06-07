package com.thiefspin.model

import anorm.{Macro, RowParser}
import org.joda.time.DateTime

final case class UssdSession(
  id: Long,
  sessionId: String,
  msisdn: String,
  currentMenu: Menu,
  countryId: Option[Long],
  amount: Option[Double],
  completed: Boolean,
  lastUpdated: DateTime
)

object UssdSession {
  val parser: RowParser[UssdSession] = Macro.indexedParser[UssdSession]

  def from(sessionId: String, msisdn: String, menu: Menu, countryId: Long): UssdSession = {
    UssdSession(
      -1L,
      sessionId,
      msisdn,
      menu,
      Option(countryId),
      None,
      false,
      DateTime.now
    )
  }
}
