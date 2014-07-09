package org.planner.controllers

import com.wordnik.swagger.annotations._
import org.planner.modules.core.ProjectModule
import org.planner.modules.dto._
import org.planner.modules._
import play.api.libs.json.Json
import play.api.mvc._
import scaldi._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

@Api(value = "/project", description = "Operations about projects")
class ProjectController(implicit val inj: Injector) extends BaseController {
  implicit val projectService = inject[ProjectModule]

  @ApiOperation(value = "Create asset", notes = "Create asset", response = classOf[ProjectDTO], httpMethod = "POST", nickname = "createProject")
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

}
