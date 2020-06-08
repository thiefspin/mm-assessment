package com.thiefspin.service

object UserService {

  /**
   * Checks whether a user exists in the system via a msisdn lookup.
   * @param msisdn
   */

  def userExists(msisdn: String): Boolean = {
    //We would need to check if a valid user is making a request.
    //We don't have a user base in this project so a mock will do.
    val invalidMock = "1234"
    msisdn != invalidMock
  }

}
