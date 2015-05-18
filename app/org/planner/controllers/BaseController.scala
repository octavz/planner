package org.planner.controllers

import org.planner.dal.{Oauth2DALComponent}

import org.planner.db.User
import org.planner.modules.core.BaseModule
import org.planner.modules.dto.JsonFormats
import play.api.mvc._

import scala.concurrent._
import ExecutionContext.Implicits.global
import scalaoauth2.provider.{AuthInfo, OAuth2AsyncProvider, ProtectedResource}

trait BaseController
  extends Controller
  with OAuth2AsyncProvider
  with Oauth2DALComponent
  with JsonFormats {
  this: BaseModule =>

  //val authHandler = inject[Oauth2DAL]

  def authorize[A](callback: AuthInfo[User] => Future[Result])(implicit request: play.api.mvc.Request[A]): Future[Result] = {
   val f = ProtectedResource.handleRequest(request, dalAuth) flatMap {
      case Left(e) if e.statusCode == 400 => Future.successful(BadRequest.withHeaders(responseOAuthErrorHeader(e)))
      case Left(e) if e.statusCode == 401 => Future.successful(Unauthorized.withHeaders(responseOAuthErrorHeader(e)))
      case Right(authInfo) =>
        authData = authInfo
        callback(authInfo)
    }

    f recover {
      case e: Throwable =>
        BadRequest.withHeaders(("500", e.getMessage))
    }

  }

}
