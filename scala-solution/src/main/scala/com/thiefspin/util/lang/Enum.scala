package com.thiefspin.util.lang

import java.sql.PreparedStatement
import play.api.libs.json._
import anorm.{Column, MetaDataItem, ParameterMetaData, ToStatement, TypeDoesNotMatch}

/**
 * Trait to that defines JSON (de)serialization to sealed traits / case objects.
 *
 * ==Example Usage==
 * {{{
 *   sealed trait EventType extends EventType.Value
 *
 *   object EventType extends Enum[EventType] {
 *     val values = List(CREATE, UPDATE)
 *
 *     case object CREATE extends EventType
 *
 *     case object UPDATE extends EventType
 *   }
 * }}}
 *
 * @tparam A The Enum type
 */
trait Enum[A] {

  trait Value {
    self: A =>
  }

  val values: Seq[A]

  def parse(v: String): Option[A] = values.find(_.toString == v)

  def error(enumName: String): JsError = JsError(s"String value '$enumName' is not a valid enum item ")

  implicit def reads: Reads[A] = {
    case JsString(v) => parse(v) match {
      case Some(a) => JsSuccess(a)
      case _ => error(v)
    }
    case _ => JsError("String value expected")
  }

  implicit def writes: Writes[A] = (v: A) => JsString(v.toString)

  implicit def format: Format[A] = Format(reads, writes)

  implicit def anormParser: Column[A] =
    Column.nonNull { (value, meta) =>
      val MetaDataItem(qualified, _, _) = meta
      value match {
        case x: String => parse(x) match {
          case Some(x) => Right(x)
          case None => Left(TypeDoesNotMatch(s"No enum for: $x"))
        }
        case _ => Left(TypeDoesNotMatch(s"Cannot convert $value: ${value.asInstanceOf[AnyRef].getClass} to DeliveryType for column $qualified"))
      }
    }

  implicit def anormFormatter: ToStatement[A] = new ToStatement[A] {
    override def set(statement: PreparedStatement, index: Int, value: A): Unit = {
      val jdbcType = java.sql.Types.VARCHAR
      if (value == null)
        statement.setNull(index, jdbcType)
      else statement.setString(index, value.toString)
    }
  }

  implicit def anormParameter: ParameterMetaData[A] = new ParameterMetaData[A] {
    override val sqlType = "VARCHAR"
    override val jdbcType: Int = java.sql.Types.VARCHAR
  }
}
