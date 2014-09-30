package org.planner.db

import java.sql.Timestamp

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
 * Created by octav on 19.08.2014.
 */
trait ModelJson {
  implicit val rds: Reads[Timestamp] = (__ \ "time").read[Long].map { long => new Timestamp(long)}
  implicit val wrs: Writes[Timestamp] = (__ \ "time").write[Long].contramap { (a: Timestamp) => a.getTime}
  implicit val fmt: Format[Timestamp] = Format(rds, wrs)
  implicit val fmtUser = Json.format[User]
  implicit val fmtUserSession = Json.format[UserSession]

}
