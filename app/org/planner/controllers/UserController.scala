package org.planner.controllers

import com.wordnik.swagger.annotations.{ApiImplicitParam, ApiImplicitParams, ApiOperation, Api}
import org.planner.modules.core.{UserModuleComponent}
import org.planner.modules.dto._
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

@Api(value = "/user", description = "User operations")
trait UserController extends BaseController {
  this: UserModuleComponent =>
  //val userModule = inject[UserModule]

  @ApiOperation(value = "Issue access token", notes = """{"token_type": "Bearer","access_token": "MDEwNTBkNDgtNDhkNC00YmNhLWJiMjktMzVhMTJkMjMwNDBk","expires_in": 3600,"refresh_token": "NzVmYjQ4ZDMtMjY3NS00NDA4LTkyZTgtNmNjOTNlNjRhNDZl"}""", response = classOf[String], httpMethod = "POST", nickname = "createAccessToken")
  def accessToken = Action.async { implicit request =>
    issueAccessToken(dalAuth)
  }

  def login = Action {
    Ok(views.html.users.login.render())
  }

  //  val loginForm = Form(
  //    mapping(
  //      "email" -> email,
  //      "password" -> nonEmptyText(5)
  //    )(LoginForm.apply)(LoginForm.unapply)
  //  )

  def loginPost = Action.async {
    implicit request =>
      userModule.login(request) map {
        case Right(r) => Redirect("/public.html").withCookies(Cookie("access_token", r.accessToken))
        //Ok(Json.toJson(responseAccessToken(r)))
        case _ => Ok(views.html.users.login.render())
      }
  }

  @ApiOperation(value = "Register user", notes = "Create new user", response = classOf[ProjectDTO], httpMethod = "POST", nickname = "registerUser")
  @ApiImplicitParams(Array(new ApiImplicitParam(value = "User to be registered", required = true, dataType = "UserDTO", paramType = "body")))
  def register = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          val dto = json.as[UserDTO]

          userModule.registerUser(dto) map (r => Ok(Json.toJson(r)))
        } catch {
          case e: Throwable =>
            Future.successful(BadRequest(s"Wrong json: ${e.getMessage}"))
        }
      }.getOrElse(Future.successful(BadRequest("Wrong json")))
  }

  @ApiOperation(value = "Add user group", notes = "Add user group", response = classOf[GroupDTO], httpMethod = "POST", nickname = "createGroup")
  @ApiImplicitParams(Array(new ApiImplicitParam(value = "New group", required = true, dataType = "GroupDTO", paramType = "body")))
  def addGroup = Action.async {
    implicit request =>
      request.body.asJson.map {
        json => try {
          val dto = json.as[GroupDTO]
          userModule.addGroup(dto) map (r => Ok(Json.toJson(r)))
        } catch {
          case e: Throwable =>
            Future.successful(BadRequest(s"Wrong json: ${e.getMessage}"))
        }
      }.getOrElse(Future.successful(BadRequest("Wrong json")))
  }
}
