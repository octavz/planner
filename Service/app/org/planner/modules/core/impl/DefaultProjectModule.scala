package org.planner.modules.core.impl

import org.planner.dal._
import org.planner.modules.core.ProjectModule
import org.planner.modules.dto._
import scaldi.Injectable._
import scaldi.Injector
import org.planner.modules._

import scala.concurrent.ExecutionContext.Implicits._

class DefaultProjectModule(implicit inj: Injector) extends ProjectModule {
  override val dal = inject[ProjectDAL]

  override def insertProject(project: ProjectDTO): Result[ProjectDTO] = {
    val userId = authData.user.id
    dal.insertProject(project.toModel(userId)) flatMap {
      case Left(err) => resultError(500, err, "insertProject")
      case Right(v) => result(new ProjectDTO(v))
    }
  }
}
