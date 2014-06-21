package controllers

import dao.impl.SlickUserDAO
import dto.LoginForm
import play.api.mvc.{Action, Controller}
import scalaoauth2.provider.OAuth2Provider
import play.api.data._
import play.api.data.Forms._
import scaldi.{Injectable, Injector}
import services.UserService

class UserController(implicit inj: Injector) extends Controller with OAuth2Provider with Injectable {
  val userService = inject[UserService]

  def accessToken = Action { implicit request =>
    issueAccessToken(new SlickUserDAO)
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
      val frm = loginForm.bindFromRequest.get
      Ok(s"${frm.email} ${frm.password}")
  }
}
