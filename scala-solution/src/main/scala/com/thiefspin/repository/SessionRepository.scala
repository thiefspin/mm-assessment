package com.thiefspin.repository

import anorm.SqlParser.scalar
import anorm.SqlStringInterpolation
import com.thiefspin.model.UssdSession
import com.thiefspin.util.database.Repository
import org.joda.time.DateTime
import anorm.JodaParameterMetaData._  //IJ removes this on code format for some reason

object SessionRepository extends Repository {

  def insert(session: UssdSession): Option[Long] = {
    withConnection { implicit c =>
      SQL"""
         INSERT INTO ussd.sessions
         (session_id,
          msisdn,
          current_menu,
          country_id,
          amount,
          completed,
          last_updated)
        VALUES (
        ${session.sessionId},
        ${session.msisdn},
        ${session.currentMenu},
        ${session.countryId},
        ${session.amount},
        false,
        ${DateTime.now}
        )
       """.executeInsert(scalar[Long].singleOpt)
    }
  }

  def update(session: UssdSession): Option[Long] = {
    withConnection { implicit c =>
      SQL"""
         UPDATE ussd.sessions
         SET msisdn = ${session.msisdn},
         current_menu = ${session.currentMenu},
         country_id = ${session.countryId},
         amount = ${session.amount},
         completed = ${session.completed},
         last_updated = ${session.lastUpdated}
       """.executeInsert(scalar[Long].singleOpt)
    }
  }

  def get(id: Long): Option[UssdSession] = {
    withConnection { implicit c =>
      SQL"""
         SELECT * FROM ussd.sessions
         WHERE id = $id
       """.as(UssdSession.parser.singleOpt)
    }
  }

  def getOpenSession(sessionId: String): Option[UssdSession] = {
    withConnection { implicit c =>
      SQL"""
         SELECT * FROM ussd.sessions
         WHERE session_id = $sessionId
         AND completed = false
       """.as(UssdSession.parser.singleOpt)
    }
  }

}
