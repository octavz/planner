package org.planner.modules.core.impl

import org.planner.dal._
import org.planner.modules.core.{ProjectModuleComponent}
import org.planner.modules.dto._
import play.api.http.Status
import org.planner.modules._
import org.planner.db._
import org.planner.util.Gen._
import org.planner.util.Time._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits._

trait DefaultProjectModuleComponent extends ProjectModuleComponent {
  this: ProjectDALComponent with UserDALComponent =>
  val projectModule = new DefaultProjectModule

  class DefaultProjectModule extends ProjectModule {

    override def insertProject(dto: ProjectDTO): Result[ProjectDTO] = {
      val project = dto.toModel(userId)
      val group = Group(id = guid, projectId = project.id, name = project.name, created = now, updated = now, userId = authData.user.id, groupId = None, perm = PermProject.OwnerReadWriteDelete)
      val f = projectDal.insertProject(project, group) map (p => resultSync(new ProjectDTO(p, group)))
      f recover { case e: Throwable => resultExSync(e, "addGroup")}
    }

    override def getUserProjects(id: String, offset: Int, count: Int): Result[ProjectListDTO] = {
      println(s"Wanted for: $id - current user: $userId")
      val f = for {
        projects <- if (id == userId) projectDal.getUserProjects(userId, offset, count) else projectDal.getUserPublicProjects(id, offset, count)
      } yield resultSync(ProjectListDTO(items = projects.map(p => new ProjectDTO(p._2, p._1)).distinct))

      f recover { case e: Throwable => resultExSync(e, "getUserProject")}
    }

    override def updateProject(dto: ProjectDTO): Result[ProjectDTO] = {
      if (dto.id.isEmpty) Future.failed(new Exception("Project has empty id"))
      else {
        projectDal.getProjectById(dto.id.get) flatMap {
          case None => resultError(Status.NOT_FOUND, "Project not found")
          case Some(project) => projectDal.updateProject(dto.toModel(authData.user.id)) map { p =>
            resultSync(new ProjectDTO(p, Group(id = "", projectId = p.id, name = "", userId = "user", groupId = None, created = p.created, updated = p.updated)))
          }
        }
      }
    }
  }

}
