package org.planner.controllers

import org.planner.dal.Oauth2DAL

import org.planner.db.User
import org.planner.modules.core.BaseModule
import org.planner.modules.dto.JsonFormats
import play.api.mvc._
import scaldi.{Injectable, Injector}

import scala.concurrent._
import ExecutionContext.Implicits.global
import scalaoauth2.provider.{AuthInfo, OAuth2AsyncProvider, ProtectedResource}

trait BaseController
  extends Controller
  with OAuth2AsyncProvider
  with Injectable
  with JsonFormats {

  val authHandler = inject[Oauth2DAL]
  implicit val inj: Injector

  def authorize[A, B <: BaseModule](callback: AuthInfo[User] => Future[Result])(implicit request: play.api.mvc.Request[A], service: B): Future[Result] = {
    ProtectedResource.handleRequest(request, authHandler) flatMap {
      case Left(e) if e.statusCode == 400 => Future.successful(BadRequest.withHeaders(responseOAuthErrorHeader(e)))
      case Left(e) if e.statusCode == 401 => Future.successful(Unauthorized.withHeaders(responseOAuthErrorHeader(e)))
      case Right(authInfo) =>
        service.authData = authInfo
        callback(authInfo)
    }
  }

}
