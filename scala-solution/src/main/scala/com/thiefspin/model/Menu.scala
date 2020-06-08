package com.thiefspin.model

import com.thiefspin.util.lang.Enum

trait Menu extends Menu.Value {

  def id: Int
}

object Menu extends Enum[Menu] {

  override val values: Seq[Menu] = Seq(
    Menu1,
    Menu2,
    Menu3,
    Menu4
  )

  case object Menu1 extends Menu {

    override def id: Int = 1

    def message(f: List[Country]): String = {
      s"Welcome to Mama Money! Where would you like to send money today? ${f.map(c => s"\n${c.id}) ${c.name}").mkString}"
    }

  }

  case object Menu2 extends Menu {

    override def id: Int = 2

    def message(session: UssdSession)(f: Long => Option[Country]): String = {
      s"How much money (in Rands) would you like to send to ${session.countryId.flatMap(id => f(id).map(_.name)).getOrElse("")}?"
    }
  }

  case object Menu3 extends Menu {

    override def id: Int = 3

    def message(value: Double, currencyCode: String): String = {
      s"Your person you are sending to will receive: $value $currencyCode \n1) OK"
    }
  }

  case object Menu4 extends Menu {

    override def id: Int = 4

    def message: String = {
      "Thank you for using Mama Money!"
    }
  }

}
