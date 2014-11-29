package org.planner.controllers

import javax.ws.rs.{QueryParam, PathParam}

import com.wordnik.swagger.annotations._
import org.planner.modules.core.{ProjectModuleComponent}
import org.planner.modules.dto._
import play.api.mvc._
import org.planner.config._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

@Api(value = "/project", description = "Project operations")
trait ProjectController extends BaseController {
  this: ProjectModuleComponent =>
  implicit val service = projectModule

  @ApiOperation(value = "Create project", notes = "Create project", response = classOf[ProjectDTO], httpMethod = "POST", nickname = "createProject")
  @ApiImplicitParams(Array(new ApiImplicitParam(value = "The new project to be added", required = true, dataType = "ProjectDTO", paramType = "body")))
  def insertProject = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          authorize {
            implicit authInfo =>
              val dto = json.as[ProjectDTO]
              projectModule.insertProject(dto) map (responseOk(_))
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
              projectModule.updateProject(dto) map (responseOk(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }
      }.getOrElse(asyncBadRequest(new Exception("Bad Json")))
  }

  @ApiOperation(value = "Get user projects", notes = "Get user projects", response = classOf[ProjectListDTO], httpMethod = "GET", nickname = "getUserProjects")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "Authorization", value = "authorization", defaultValue = "OAuth token", required = true, dataType = "string", paramType = "header")
  ))
  def getUserProjects(
                       @ApiParam(value = "user id", required = true) @PathParam(value = "id") id: String,
                       @ApiParam(value = "offset", required = true, defaultValue = "0") @QueryParam(value = "offset") offset: Int,
                       @ApiParam(value = "count", required = true, defaultValue = "100") @QueryParam(value = "count") count: Int) =
    Action.async {
      implicit request =>
        try {
          authorize {
            implicit authInfo =>
              projectModule.getUserProjects(id, offset, count) map (responseOk(_))
          }
        } catch {
          case e: Throwable => asyncBadRequest(e)
        }

    }

}
