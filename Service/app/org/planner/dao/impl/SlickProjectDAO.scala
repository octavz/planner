package org.planner.dao.impl

import org.planner.dao._
import org.planner.db._
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB

import scala.concurrent._

class SlickProjectDAO extends ProjectDAO with DB {


}
