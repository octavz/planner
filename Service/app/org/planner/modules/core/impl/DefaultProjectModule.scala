package org.planner.modules.core.impl

import org.planner.dal._
import org.planner.modules.core.ProjectModule
import org.planner.modules.dto._
import play.api.http.Status
import scaldi.Injectable._
import scaldi.Injector
import org.planner.modules._

import scala.concurrent.ExecutionContext.Implicits._

class DefaultProjectModule(implicit inj: Injector) extends ProjectModule {
  override val dal = inject[ProjectDAL]

  override def insertProject(project: ProjectDTO): Result[ProjectDTO] = {
    dal.insertProject(project.toModel(userId)) flatMap {
      case Left(err) => resultError(Status.INTERNAL_SERVER_ERROR, err, "insertProject")
      case Right(v) => result(new ProjectDTO(v))
    }
  }

  override def getUserProjects(): Result[ProjectListDTO] = {
    val f = dal.getUserProjects(authData.user.id) flatMap {
      case Left(err) => resultError(Status.INTERNAL_SERVER_ERROR, err, "getUserProjects")
      case Right(v) => result(ProjectListDTO(items = v.map(new ProjectDTO(_))))
    }

    f recoverWith { case e: Throwable => resultError(Status.INTERNAL_SERVER_ERROR, e.getMessage)}
  }
}
