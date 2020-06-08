package com.thiefspin.model

import anorm.{Macro, RowParser}

final case class Country(
  id: Long,
  name: String,
  currencyCode: String,
  currencyValue: Double
)

object Country {
  val parser: RowParser[Country] = Macro.indexedParser[Country]
}
