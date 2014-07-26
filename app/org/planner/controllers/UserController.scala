package org.planner.controllers

import com.wordnik.swagger.annotations.{ApiImplicitParam, ApiImplicitParams, ApiOperation, Api}
import org.planner.modules.core.UserModule
import org.planner.modules.dto._
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.Json
import play.api.mvc.{Action, Cookie, SimpleResult}
import scaldi.Injector

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

@Api(value = "/user", description = "User operations")
class UserController(implicit val inj: Injector) extends BaseController {
  val userService = inject[UserModule]

  @ApiOperation(value = "Issue access token", notes = """{"token_type": "Bearer","access_token": "MDEwNTBkNDgtNDhkNC00YmNhLWJiMjktMzVhMTJkMjMwNDBk","expires_in": 3600,"refresh_token": "NzVmYjQ4ZDMtMjY3NS00NDA4LTkyZTgtNmNjOTNlNjRhNDZl"}""", response = classOf[String], httpMethod = "POST", nickname = "createAccessToken")
  def accessToken = Action.async { implicit request =>
    issueAccessToken(authHandler) map (_.asInstanceOf[SimpleResult])
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

  def loginPost = Action {
    implicit request =>
      userService.login(request) match {
        //        case Left(e) if e.statusCode == 400 => BadRequest(responseOAuthErrorJson(e)).withHeaders(responseOAuthErrorHeader(e))
        //        case Left(e) if e.statusCode == 401 => Unauthorized(responseOAuthErrorJson(e)).withHeaders(responseOAuthErrorHeader(e))
        case Right(r) =>
          Redirect("/public.html").withCookies(Cookie("access_token", r.accessToken))
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

          userService.registerUser(dto) map (r => Ok(Json.toJson(r)))
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
          userService.addGroup(dto) map (r => Ok(Json.toJson(r)))
        } catch {
          case e: Throwable =>
            Future.successful(BadRequest(s"Wrong json: ${e.getMessage}"))
        }
      }.getOrElse(Future.successful(BadRequest("Wrong json")))
  }
}
