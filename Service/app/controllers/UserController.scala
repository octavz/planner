package controllers

import play.api.mvc.{Action, Controller}
import scalaoauth2.provider.OAuth2Provider
import oauth.OAuthDataHandler
import play.api.data._
import play.api.data.Forms._
import scaldi.{Injectable, Injector}
import services.UserService

class UserController(implicit inj: Injector) extends Controller with OAuth2Provider with Injectable {
  val userService = inject [UserService]

  def accessToken = Action { implicit request =>
    issueAccessToken(new OAuthDataHandler)
  }

  def login = Action {
    Ok(views.html.users.login.render())
  }

  val loginForm = Form (
      tuple(
        "email" -> email,
        "password" -> nonEmptyText(5)
        )
      )

  def loginPost = Action {
    implicit request =>
      val (user, password) = loginForm.bindFromRequest.get
      Ok(s"$user $password")
  }
}
