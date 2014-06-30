package org.planner.controllers

import play.api.mvc.{Action, Controller}
import scaldi.{Injectable, Injector}

import scalaoauth2.provider.OAuth2Provider

class MainController(implicit inj: Injector) extends Controller with OAuth2Provider with Injectable {

  def build = Action { _ => Ok("build") }

  def index = Action {
    implicit request =>
      Ok("index")
  }
}
