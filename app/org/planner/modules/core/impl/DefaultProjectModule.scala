package org.planner.modules.core.impl

import org.planner.dal._
import org.planner.modules.core.ProjectModule
import org.planner.modules.dto._
import play.api.http.Status
import scaldi.Injectable._
import scaldi.Injector
import org.planner.modules._
import org.planner.db._
import org.planner.util.Gen._
import org.planner.util.Time._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits._

class DefaultProjectModule(implicit inj: Injector) extends ProjectModule {
  override val dal = inject[ProjectDAL]
  override val userDal = inject[UserDAL]

  override def insertProject(dto: ProjectDTO): Result[ProjectDTO] = {
    val project = dto.toModel(userId)
    val group = Group(id = guid, projectId = project.id, name = project.name, created = now, updated = now, userId = authData.user.id, groupId = None, perm = PermProject.OwnerReadWriteDelete)
    val f = dal.insertProject(project, group) map (p => resultSync(new ProjectDTO(p, group)))
    f recover { case e: Throwable => resultExSync(e, "addGroup")}
  }

  override def getUserProjects(id: String, offset: Int, count: Int): Result[ProjectListDTO] = {
    val f = for {
      projects <- if (id == userId) dal.getUserProjects(userId, offset, count) else dal.getUserPublicProjects(id, offset, count)
    } yield resultSync(ProjectListDTO(items = projects.map(p => new ProjectDTO(p._2, p._1))))

    f recover { case e: Throwable => resultExSync(e, "getUserProject")}
  }

  override def updateProject(dto: ProjectDTO): Result[ProjectDTO] = {
    if (dto.id.isEmpty) Future.failed(new Exception("Project has empty id"))
    else {
      dal.getProjectById(dto.id.get) flatMap {
        case None => resultError(Status.NOT_FOUND, "Project not found")
        case Some(project) => dal.updateProject(dto.toModel(authData.user.id)) map { p =>
          resultSync(new ProjectDTO(p, Group(id = "", projectId = p.id, name = "", userId = "user", groupId = None, created = p.created, updated = p.updated)))
        }
      }
    }
  }
}
