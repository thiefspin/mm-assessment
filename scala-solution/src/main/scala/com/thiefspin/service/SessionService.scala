package com.thiefspin.service

import com.thiefspin.model.Menu.{Menu1, Menu2, Menu3, Menu4}
import com.thiefspin.model.{Country, UssdRequest, UssdResponse, UssdSession}
import com.thiefspin.repository.{CountryRepository, SessionRepository}

object SessionService {

  def handleRequest(req: UssdRequest): Option[UssdResponse] = {
    if (UserService.userExists(req.msisdn)) {
      withUserEntry(req.userEntry) { userEntry =>
        withSession(req.sessionId) { session =>
          whenSessionComplete(session)(completeFlow(session)) {
            menu3Flow(session, userEntry)
          }
        }(menu2Flow(userEntry, req))
      }(menu1Flow(req.sessionId))
    } else {
      None
    }
  }

  def getCurrentSession(sessionId: String): Option[UssdSession] = {
    SessionRepository.getOpenSession(sessionId)
  }

  private def completeFlow(session: UssdSession): Option[UssdResponse] = {
    SessionRepository.update(session.copy(completed = true))
    Option(UssdResponse(session.sessionId, Menu4.message))
  }

  private def menu3Flow(session: UssdSession, userEntry: String): Option[UssdResponse] = {
    val newSession = session.copy(amount = Option(userEntry.toDouble), currentMenu = Menu2)
    SessionRepository.update(newSession)
    CountryRepository.get(newSession.countryId.getOrElse(-1L)).map { country =>
      UssdResponse(session.sessionId, Menu3.message(calculateCurrencyConversion(newSession.amount.getOrElse(0.00), country), country.currencyCode))
    }
  }

  private def menu2Flow(userEntry: String, req: UssdRequest): Option[UssdResponse] = {
    CountryRepository.get(userEntry.toLong).flatMap { country =>
      SessionRepository.insert(UssdSession.from(req.sessionId, req.msisdn, Menu1, country.id)).flatMap { id =>
        SessionRepository.get(id).map { session =>
          UssdResponse(session.sessionId, Menu2.message(session)(CountryRepository.get))
        }
      }
    }
  }

  private def menu1Flow(sessionId: String): Option[UssdResponse] = {
    Option(UssdResponse(sessionId, Menu1.message(CountryRepository.list())))
  }

  private def whenSessionComplete[A](session: UssdSession)(complete: => A)(inComplete: => A): A = {
    if (session.currentMenu.id == 2) complete
    else inComplete
  }

  private def withUserEntry[A](optionalEntry: Option[String])(exists: String => A)(none: => A): A = {
    optionalEntry.map(exists).getOrElse(none)
  }

  private def withSession[A](sessionId: String)(exists: UssdSession => A)(none: => A): A = {
    getCurrentSession(sessionId).map(exists).getOrElse(none)
  }

  private def calculateCurrencyConversion(randValue: Double, country: Country): Double = {
    randValue * country.currencyValue
  }

}
