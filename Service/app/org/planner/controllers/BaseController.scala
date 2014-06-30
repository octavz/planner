package org.planner.controllers

import org.planner.dao.Oauth2DAO
import org.planner.db.User
import org.planner.services.dto.JsonFormats
import org.planner.services.BaseService
import play.api.mvc._
import scaldi.{Injectable, Injector}

import scala.concurrent._
import scalaoauth2.provider.{AuthInfo, OAuth2AsyncProvider, ProtectedResource}

trait BaseController
  extends Controller
  with OAuth2AsyncProvider
  with Injectable
  with JsonFormats {

  val authHandler = inject[Oauth2DAO]
  implicit val inj: Injector

  def authorize[A, B <: BaseService](callback: AuthInfo[User] => Future[SimpleResult])(implicit request: play.api.mvc.Request[A], service: B): Future[SimpleResult] = {
    ProtectedResource.handleRequest(request, authHandler) match {
      case Left(e) if e.statusCode == 400 => Future.successful(BadRequest.withHeaders(responseOAuthErrorHeader(e)))
      case Left(e) if e.statusCode == 401 => Future.successful(Unauthorized.withHeaders(responseOAuthErrorHeader(e)))
      case Right(authInfo) =>
        service.authInfo = authInfo
        callback(authInfo)
    }
  }

}
