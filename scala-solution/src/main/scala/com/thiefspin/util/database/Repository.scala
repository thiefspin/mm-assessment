package com.thiefspin.util.database

import java.sql.Connection

trait Repository {

  /**
   * Provides a jdbc connection.
   *
   * @param f Function connection will apply to
   * @tparam A Return type
   * @return A
   */

  def withConnection[A](f: Connection => A): A = {
    ConnectionPool.withConnection(f)
  }

}
