package com.thiefspin.web.ussd

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.thiefspin.model.UssdRequest
import com.thiefspin.service.SessionService
import com.thiefspin.util.json.JsonParser
import com.thiefspin.web.responses.HttpError
import com.typesafe.scalalogging.LazyLogging
import scalaz.{-\/, \/-}

object SessionRoute extends LazyLogging {

  def routes: Route = ussdRoute

  def ussdRoute: Route = path("api" / "ussd") {
    post {
      entity(as[String]) { body =>
        JsonParser[UssdRequest](body) match {
          case \/-(req) => SessionService.handleRequest(req) match {
            case Some(res) => complete(res.asJsonString)
            case None => complete(
              HttpResponse(
                StatusCodes.NotFound,
                entity = HttpError(StatusCodes.NotFound).apply
              )
            )
          }
          case -\/(err) => {
            logger.warn(err.error)
            complete(
              HttpResponse(
                StatusCodes.BadRequest,
                entity = HttpError(StatusCodes.BadRequest).apply
              )
            )
          }
        }
      }
    }
  }

}
