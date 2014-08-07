package org.planner.controllers

import controllers.Assets
import play.api.Play
import play.api.mvc.{AnyContent, Action, Controller}
import scaldi.{Injectable, Injector}

import scalaoauth2.provider.OAuth2Provider
import play.api.Play.current
import org.planner.config._

class MainController(implicit inj: Injector) extends Controller with OAuth2Provider with Injectable {

  def build = Action { _ => Ok("build")}

  def indexUser(user: String) = Action.async {
    implicit request =>
      Assets.at("/public/client", "index.html")(request)
  }

  def indexProject(user: String, project: String) = Action.async {
    implicit request =>
      Assets.at("/public/client", "index.html")(request)
      //Ok(views.html.users.index.render())
  }

  def options(path:String) = Action { Ok("")}
}
