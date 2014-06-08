package controllers

import play.api.mvc.{Action, Controller}
import scalaoauth2.provider.OAuth2Provider
import models.Clients
import scaldi.{Injectable, Injector}

class MainController(implicit inj: Injector) extends Controller with OAuth2Provider with Injectable {

  def build = Action { _ => Ok(Clients.create.toString) }

  def index = Action {
    implicit request =>
      Ok("index")
  }
}
