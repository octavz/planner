package org.planner.controllers

import javax.ws.rs.PathParam

import com.wordnik.swagger.annotations._
import org.planner.modules.core.ProjectModule
import org.planner.modules.dto._
import org.planner.modules._
import play.api.libs.json.Json
import play.api.mvc._
import scaldi._

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
              projectService.insertProject(dto) map (r => Ok(Json.toJson(r)))
          }
        } catch {
          case e: Throwable =>
            Future.successful(BadRequest(s"Wrong json: ${e.getMessage}"))
        }
      }.getOrElse(Future.successful(BadRequest("Wrong json")))
  }

  @ApiOperation(value = "Get user projects", notes = "Get user projects", response = classOf[ProjectListDTO], httpMethod = "GET", nickname = "getUserProjects")
  def getUserProjects(@ApiParam(value = "id", required = true) @PathParam("id") id: String) =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              projectService.getUserProjects() map (r => Ok(Json.toJson(r)))
          }
        } catch {
          case e: Throwable =>
            Future.successful(BadRequest(s"Wrong json: ${e.getMessage}"))
        }

    }

}
