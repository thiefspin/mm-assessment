package com.thiefspin.web

import akka.http.scaladsl.server.Route
import com.thiefspin.web.health.HealthRoute
import com.thiefspin.web.ussd.SessionRoute
import akka.http.scaladsl.server.Directives._

trait Routes {

  implicit val allRoutes: Route = HealthRoute.routes ~ SessionRoute.routes

}
