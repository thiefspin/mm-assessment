package com.thiefspin

import com.thiefspin.configuration.Configuration
import com.thiefspin.web.{Routes, WebServer}

object Server extends WebServer with App with Routes {

  import Implicits._

  startWebServer(
    Configuration.getInt("application.web.port"),
    Configuration.getString("application.web.interface")
  )

}
