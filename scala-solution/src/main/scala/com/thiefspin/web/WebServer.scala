package com.thiefspin.web

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future

trait WebServer extends LazyLogging {

  def startWebServer(port: Int, interface: String)
    (implicit system: ActorSystem, routes: Route): Future[Http.ServerBinding] = {
    logger.info(s"Starting application: $interface:$port")
    Http().bindAndHandle(routes, interface, port)
  }

}
