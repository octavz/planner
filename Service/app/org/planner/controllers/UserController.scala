package org.planner.controllers

import org.planner.services.dto._
import org.planner.services.UserService
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.Json
import play.api.mvc.{Action, Cookie, SimpleResult}
import scaldi.Injector

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class UserController(implicit val inj: Injector) extends BaseController {
  val userService = inject[UserService]

  def accessToken = Action.async { implicit request =>
    issueAccessToken(authHandler) map ( _.asInstanceOf[SimpleResult])
  }

  def login = Action {
    Ok(views.html.users.login.render())
  }

  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText(5)
    )(LoginForm.apply)(LoginForm.unapply)
  )

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
}
