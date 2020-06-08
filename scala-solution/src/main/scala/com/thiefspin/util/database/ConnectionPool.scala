package com.thiefspin.util.database

import java.sql.Connection

import scalikejdbc._
import scalikejdbc.config._

/**
 * Initialises a connection pool based off of the config provided.
 *
 * === Example Config ===
 * {{{
 *   db {
 *   # JDBC settings
 *   db.default.url = ${POSTGRES_URL}
 *   db.default.user = ${POSTGRES_USER}
 *   db.default.password = ${POSTGRES_PASSWORD}
 *   # Connection Pool settings
 *   db.default.poolInitialSize = 10
 *   db.default.poolMaxSize = 20
 *   db.default.connectionTimeoutMillis = 1000
 * }
 * }}}
 */

object ConnectionPool {

  DBsWithEnv("db").setupAll()

  /**
   * Provides a java.sql.Connection to execute database operations.
   *
   * @param f Function[Connection, A]
   * @tparam A Return type
   * @return A
   */

  def withConnection[A](f: Connection => A): A = {
    DB.autoCommit { implicit session =>
      f(session.connection)
    }
  }

}
