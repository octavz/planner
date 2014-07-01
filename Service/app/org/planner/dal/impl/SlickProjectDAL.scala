package org.planner.dal.impl

import org.planner.dal._
import org.planner.db._
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB

import scala.concurrent._

class SlickProjectDAL extends ProjectDAL with DB {

  def insertProject(model: Project): DAO[Project] = ???

}
