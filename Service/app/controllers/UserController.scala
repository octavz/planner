package controllers

import dao.impl.SlickUserDAO
import dto.LoginForm
import play.api.libs.json.Json
import play.api.mvc.{Cookie, Action, Controller}
import scalaoauth2.provider.OAuth2Provider
import play.api.data._
import play.api.data.Forms._
import scaldi.{Injectable, Injector}
import services.UserService

class UserController(implicit inj: Injector) extends Controller with OAuth2Provider with Injectable {
  val userService = inject[UserService]

  def accessToken = Action { implicit request =>
    issueAccessToken(userService.repoAuth)
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
}
