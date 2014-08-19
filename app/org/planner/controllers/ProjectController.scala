package org.planner.controllers

import com.wordnik.swagger.annotations._
import org.planner.modules.core.ProjectModule
import org.planner.modules.dto._
import play.api.mvc._
import scaldi._
import org.planner.config._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

@Api(value = "/project", description = "Project operations")
class ProjectController(implicit val inj: Injector) extends BaseController {
  implicit val projectService = inject[ProjectModule]

  @ApiOperation(value = "Create project", notes = "Create project", response = classOf[ProjectDTO], httpMethod = "POST", nickname = "createProject")
  @ApiImplicitParams(Array(new ApiImplicitParam(value = "The new project to be added", required = true, dataType = "ProjectDTO", paramType = "body")))
  def insertProject = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              val dto = json.as[ProjectDTO]
              projectService.insertProject(dto) map (responseOk(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
      }.getOrElse(asyncBadRequest(new Exception("Bad Json")))
  }

  @ApiOperation(value = "Update project", notes = "update project", response = classOf[ProjectDTO], httpMethod = "PUT", nickname = "updateProject")
  @ApiImplicitParams(Array(new ApiImplicitParam(value = "The project to be updated", required = true, dataType = "ProjectDTO", paramType = "body")))
  def updateProject = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              val dto = json.as[ProjectDTO]
              projectService.updateProject(dto) map (responseOk(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
      }.getOrElse(asyncBadRequest(new Exception("Bad Json")))
  }

  @ApiOperation(value = "Get user projects", notes = "Get user projects", response = classOf[ProjectListDTO], httpMethod = "GET", nickname = "getUserProjects")
  def getUserProjects(id: String, offset: Int, count: Int) =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              projectService.getUserProjects(id, offset, count) map (responseOk(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }

    }

}
