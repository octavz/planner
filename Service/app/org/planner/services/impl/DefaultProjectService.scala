package org.planner.services.impl

import java.util.Date

import org.planner.dao._
import org.planner.db._
import org.planner.services.dto._
import play.api.Logger
import scaldi.Injectable._
import scaldi.Injector
import org.planner.services._

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scalaoauth2.provider._

class DefaultProjectService(implicit inj: Injector) extends ProjectService {
  val repo = inject[ProjectDAO]

  def insertProject(project :ProjectDTO): RetService[ProjectDTO] = ???
}
