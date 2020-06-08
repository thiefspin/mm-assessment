package com.thiefspin.service

import java.sql.Connection
import java.util.UUID

import anorm.SqlStringInterpolation
import com.opentable.db.postgres.embedded.EmbeddedPostgres
import com.thiefspin.model.Menu._
import com.thiefspin.model.{Country, UssdRequest, UssdResponse}
import com.thiefspin.repository.{CountryRepository, SessionRepository}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers

class SessionServiceSpec extends AnyWordSpec with Matchers with BeforeAndAfterAll {

  var pg: EmbeddedPostgres = _

  private def initDatabase(): Unit = {
    pg = EmbeddedPostgres.builder().setPort(2551).start()
    implicit val conn: Connection = pg.getPostgresDatabase().getConnection()
    val s =
      SQL"""
        DROP SCHEMA IF EXISTS ussd CASCADE;
        CREATE SCHEMA ussd;

        CREATE TABLE ussd.countries
        (
            id             serial PRIMARY KEY,
            name           VARCHAR NOT NULL,
            currency_code  VARCHAR NOT NULL,
            currency_value decimal NOT NULL
        );

        CREATE TABLE ussd.sessions
        (
            id           serial PRIMARY KEY,
            session_id   VARCHAR   NOT NULL,
            msisdn       VARCHAR   NOT NULL,
            current_menu VARCHAR   NOT NULL,
            country_id   int REFERENCES ussd.countries (id),
            amount       DECIMAL,
            completed    BOOLEAN   NOT NULL,
            last_updated TIMESTAMP NOT NULL
        );

        INSERT INTO ussd.countries (name, currency_code, currency_value)
        VALUES ('Kenya', 'KES', 6.10),
               ('Malawi', 'MWK', 42.50);
     """.execute()
  }

  override def beforeAll(): Unit = {
    initDatabase()
  }

  override def afterAll(): Unit = {
    pg.close()
  }

  val sessionId: String = UUID.randomUUID().toString
  val msisdn: String = UUID.randomUUID().toString

  "The session service " should {

    "respond with the initial menu when user entry is empty in the request" in {
      val countries: List[Country] = CountryRepository.list()
      val req = UssdRequest(sessionId, msisdn, None)

      SessionService.handleRequest(req).map { res =>
        res shouldEqual UssdResponse(sessionId, Menu1.message(countries))
      }
    }

    "respond with the second menu when the user entry is not empty and an open session for the user does not exist" in {
      val countries: List[Country] = CountryRepository.list()
      val req = UssdRequest(sessionId, msisdn, Option(countries.head.id.toString))

      SessionService.handleRequest(req).map { res =>
        SessionRepository.getOpenSession(sessionId).map { session =>
          res shouldEqual UssdResponse(sessionId, Menu2.message(session)(CountryRepository.get))
        }
      }
    }

    "respond with the 3rd menu when the user entry is not empty and an open session exists" in {
      val countries: List[Country] = CountryRepository.list()
      val req = UssdRequest(sessionId, msisdn, Option("100.00"))

      SessionService.handleRequest(req).map { res =>
        res shouldEqual UssdResponse(sessionId, Menu3.message(100.00 * countries.head.currencyValue, countries.head.currencyCode))
      }
    }

    "respond with the 4th menu when flow has been completed" in {
      val req = UssdRequest(sessionId, msisdn, Option("OK"))
      SessionService.handleRequest(req).map { res =>
        res shouldEqual UssdResponse(sessionId, Menu4.message)
        SessionRepository.getOpenSession(sessionId) shouldEqual None
      }
    }

    "start a new process if there are no open sessions" in {
      val req = UssdRequest(sessionId, msisdn, None)
      SessionService.handleRequest(req).map { res =>
        res shouldEqual UssdResponse(sessionId, Menu1.message(CountryRepository.list))
      }
    }

    "insert open a new session if a new process is started with the same sessionId" in {
      val countries: List[Country] = CountryRepository.list()
      val req = UssdRequest(sessionId, msisdn, Option(countries.head.id.toString))

      SessionService.handleRequest(req).map { res =>
        SessionRepository.getOpenSession(sessionId).map { session =>
          res shouldEqual UssdResponse(sessionId, Menu2.message(session)(CountryRepository.get))
        }.getOrElse(assert(false))
      }
    }
  }

}
