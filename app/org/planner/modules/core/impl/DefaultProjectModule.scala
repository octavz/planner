package org.planner.modules.core.impl

import org.planner.dal._
import org.planner.modules.core.{ProjectModuleComponent}
import org.planner.modules.dto._
import org.planner.util.Constants
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
      val project = dto.toModel()
      val group = Group(id = guid, projectId = project.id, name = project.name, created = now, updated = now, userId = authData.user.id, groupId = None, perm = PermProject.OwnerReadWriteDelete)
      val f = projectDal.insertProject(project, group) map (p => resultSync(new ProjectDTO(p, group)))
      f recover { case e: Throwable => resultExSync(e, "addGroup") }
    }

    override def getUserProjects(id: String, offset: Int, count: Int): Result[ProjectListDTO] = {
      println(s"Wanted for: $id - current user: $userId")
      val f = for {
        projects <- if (id == userId) projectDal.getUserProjects(userId, offset, count) else projectDal.getUserPublicProjects(id, offset, count)
      } yield resultSync(ProjectListDTO(items = projects.map(p => new ProjectDTO(p._2, p._1)).distinct))

      f recover { case e: Throwable => resultExSync(e, "getUserProject") }
    }

    override def updateProject(dto: ProjectDTO): Result[ProjectDTO] = {
      if (dto.id.isEmpty) Future.failed(new Exception("Project has empty id"))
      else {
        checkUser(dto.userId.getOrElse(throw new Exception("User not found!")), dto.id.get) flatMap {
          res =>
            if (!res) throw new Exception("Not valid")
            projectDal.getProjectById(dto.id.get) flatMap {
              case None => resultError(Status.NOT_FOUND, "Project not found")
              case Some(project) => projectDal.updateProject(dto.toModel()) map { p =>
                resultSync(new ProjectDTO(p, Group(id = "", projectId = p.id, name = "", userId = "user", groupId = None, created = p.created, updated = p.updated)))
              }
            }
        }
      }
    }

    private def checkUser(userId: String, projectId: String): Future[Boolean] = {
      for {
        projectGroups <- projectDal.getProjectGroups(projectId)
        userGroups <- dalUser.getUserGroups(userId)
      } yield {
        projectGroups map (_.id) intersect (userGroups) nonEmpty
      }
    }

    override def insertTask(task: TaskDTO): Result[TaskDTO] = {
      projectDal.getProjectGroups(task.projectId.getOrElse(throw new Exception("Task has no group id"))) flatMap {
        groups =>
          dalUser.getUserGroups(task.userId.getOrElse(throw new Exception("User id not found"))) flatMap {
            userGroups =>
              task.groupId match {
                case Some(gid) =>
                  groups.find(_.id == gid) match {
                    case None => throw new Exception("Attched group doesn't belong to the project")
                    case _ => projectDal.insertTask(task.toModel()) map { _ => resultSync(task) }
                  }
                case None =>
                  groups.find(_.`type` == Constants.DefaultGroupType) match {
                    case Some(mainGroup) =>
                      val model = task.toModel().copy(groupId = mainGroup.projectId)
                      projectDal.insertTask(model) map { _ => resultSync(task) }
                    case None => throw new Exception(s"Project ${task.projectId.get} has no main group")
                  }
              }

          }
      }
    }
  }

}
