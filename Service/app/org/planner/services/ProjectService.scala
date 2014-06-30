package org.planner.services

import org.planner.dao._
import org.planner.db.User
import org.planner.services.dto._

import scalaoauth2.provider.{AuthInfo, GrantHandlerResult, OAuthError, AuthorizationRequest}

trait BaseService {
  private var _authInfo: AuthData = null

  def authInfo = _authInfo
  def authInfo_=(value: AuthData): Unit = _authInfo = value
}

trait ProjectService extends BaseService {
  def insertProject(project: ProjectDTO): RetService[ProjectDTO]
}
