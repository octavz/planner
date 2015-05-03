package org.planner

import play.api.Logger
import play.api.libs.json.{Json, Writes}
import play.api.mvc.Results._
import scala.concurrent._

/**
 * Created by Octav on 7/30/2014.
 */
package object controllers {

  def asyncBadRequest(ex: Throwable) = {
    Logger.error(ex.getMessage)
    Future.successful(BadRequest( s"""{"errCode":0, "errMessage":${ex.getMessage}}"""))
  }

  def asyncBadRequest(msg: String) = {
    Logger.error(msg)
    Future.successful(BadRequest( s"""{"errCode":0, "errMessage":${msg}}"""))
  }

  def responseOk[T](a: T)(implicit w: Writes[T]) = Ok(Json.toJson(a))

}
