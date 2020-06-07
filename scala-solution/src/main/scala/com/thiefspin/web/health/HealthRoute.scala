package com.thiefspin.web.health

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object HealthRoute {

  /**
   * Add routes here to enable main route discovery
   *
   * @return Route
   */
  def routes: Route = healthCheck

  /**
   * Simple health endpoint to determine whether application is responding.
   *
   * @return HealthResponse as json
   */

  def healthCheck: Route = path("api" / "health") {
    get {
      complete(HttpEntity(ContentTypes.`application/json`, HealthResponse("OK").asJsonString))
    }
  }

}
