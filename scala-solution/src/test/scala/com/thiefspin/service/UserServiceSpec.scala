package com.thiefspin.service

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.Random

class UserServiceSpec extends AnyWordSpec with Matchers {

  private val invalidMsisdn = "1234"
  private val validMsisdn = Random.nextString(5)

  "When a user check happens with a valid msisdn string the user service " should {
    "return return true" in {
      UserService.userExists(validMsisdn) shouldEqual true
    }

    "and return false if invalid" in {
      UserService.userExists(invalidMsisdn) shouldEqual false
    }
  }

}
