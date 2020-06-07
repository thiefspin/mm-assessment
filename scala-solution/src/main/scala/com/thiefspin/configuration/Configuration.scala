package com.thiefspin.configuration

import com.typesafe.config.{Config, ConfigFactory}

/**
 * Configuration helper object as singleton
 * Initialises TypeSafe config lazily once and provides access to the lazy val or helper functions.
 */

object Configuration {

  /**
   * Parses application.conf in HOCON format and load values.
   * For more:
   *
   * @see https://github.com/lightbend/config
   */

  lazy val configuration: Config = ConfigFactory.load()

  def getString(key: String): String = {
    configuration.getString(key)
  }

  def getInt(key: String): Int = {
    configuration.getInt(key)
  }

}
