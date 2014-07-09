package org.planner.dal.impl

import org.planner.dal._
import org.planner.db._
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import scaldi.Injector
import scaldi.Injectable._

import scala.concurrent._

class SlickProjectDAL(implicit inj: Injector) extends ProjectDAL with DB {
  val cache = inject[Caching]

  def insertProject(model: Project): DAL[Project] = ???

}
