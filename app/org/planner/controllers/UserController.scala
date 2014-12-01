package org.planner.controllers

import com.wordnik.swagger.annotations.{ApiImplicitParam, ApiImplicitParams, ApiOperation, Api}
import org.planner.modules.core.{UserModuleComponent}
import org.planner.modules.dto._
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

@Api(value = "/api/user", description = "User operations")
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

  @ApiOperation(value = "Login user", notes = "Login user", response = classOf[JsValue], httpMethod = "POST", nickname = "login")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "username", required = true, dataType = "String", paramType = "form", defaultValue = "aaa@aaa.com")
    , new ApiImplicitParam(name = "password", required = true, dataType = "String", paramType = "form", defaultValue = "123456")
    , new ApiImplicitParam(name = "client_id", required = true, dataType = "String", paramType = "form", defaultValue = "1")
    , new ApiImplicitParam(name = "grant_type", required = true, dataType = "String", paramType = "form", defaultValue = "password")
    , new ApiImplicitParam(name = "client_secret", required = true, dataType = "String", paramType = "form", defaultValue = "secret")
  ))
  def loginPost = Action.async {
    implicit request =>
      userModule.login(request) map {
        case Right(r) =>
          if (request.accepts("text/html")) {
            Redirect("/public.html").withCookies(Cookie("access_token", r.accessToken))
          } else {
            Ok(Json.obj("accessToken" -> r.accessToken))
          }
        case _ =>
          if (request.accepts("text/html")) {
            Ok(views.html.users.login.render())
          } else {
            Unauthorized
          }
      }
  }

  @ApiOperation(value = "Login user", notes = "Login user", response = classOf[JsValue], httpMethod = "POST", nickname = "login")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "username", required = true, dataType = "String", paramType = "form", defaultValue = "aaa@aaa.com")
    , new ApiImplicitParam(name = "password", required = true, dataType = "String", paramType = "form", defaultValue = "123456")
    , new ApiImplicitParam(name = "client_id", required = true, dataType = "String", paramType = "form", defaultValue = "1")
    , new ApiImplicitParam(name = "grant_type", required = true, dataType = "String", paramType = "form", defaultValue = "password")
    , new ApiImplicitParam(name = "client_secret", required = true, dataType = "String", paramType = "form", defaultValue = "secret")
  ))
  def loginApi = Action.async {
    implicit request =>
      userModule.login(request) map {
        case Right(r) =>
          if (request.accepts("text/html")) {
            Redirect("/public.html").withCookies(Cookie("access_token", r.accessToken))
          } else {
            Ok(Json.obj("accessToken" -> r.accessToken))
          }
        case _ =>
          if (request.accepts("text/html")) {
            Ok(views.html.users.login.render())
          } else {
            Unauthorized
          }
      }
  }

  @ApiOperation(value = "Register user", notes = "Create new user", response = classOf[ProjectDTO], httpMethod = "POST", nickname = "registerUser")
  @ApiImplicitParams(Array(new ApiImplicitParam(value = "User to be registered", required = true, dataType = "org.planner.modules.dto.UserDTO", paramType = "body")))
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
