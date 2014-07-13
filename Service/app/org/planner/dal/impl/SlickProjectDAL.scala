package org.planner.dal.impl

import org.planner.dal._
import org.planner.db._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import scaldi.Injector
import scaldi.Injectable._

import scala.concurrent._

class SlickProjectDAL(implicit inj: Injector, app: play.api.Application) extends ProjectDAL with DB {
  val cache = inject[Caching]

  def insertProject(model: Project): DAL[Project] =
    DB.withSession {
      implicit session =>
        Projects.insert(model)
        dal(model)
    }

  override def getUserProjects(uid: String): DAL[List[Project]] =
    DB.withSession {
      implicit session =>
        val ret = Projects.where(_.userId === uid).list()
        dal(ret)
    }
}
