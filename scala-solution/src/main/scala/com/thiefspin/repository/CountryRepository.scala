package com.thiefspin.repository

import anorm.SqlStringInterpolation
import com.thiefspin.model.Country
import com.thiefspin.util.database.Repository

object CountryRepository extends Repository {

  def list(): List[Country] = {
    withConnection { implicit c =>
      SQL"""
         SELECT * FROM ussd.countries
       """.as(Country.parser.*)
    }
  }

  def get(id: Long): Option[Country] = {
    withConnection { implicit c =>
      SQL"""
         SELECT * FROM ussd.countries
         WHERE id = $id
       """.as(Country.parser.singleOpt)
    }
  }

  def getByName(name: String): Option[Country] = {
    withConnection { implicit c =>
      SQL"""
         SELECT * FROM ussd.countries
         WHERE name = $name
       """.as(Country.parser.singleOpt)
    }
  }

}
