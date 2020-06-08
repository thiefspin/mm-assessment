package com.thiefspin

import akka.actor.ActorSystem

object Implicits {

  lazy implicit val applicationName: String = "ScalaSolution"

  lazy implicit val system: ActorSystem = ActorSystem(applicationName)

}
