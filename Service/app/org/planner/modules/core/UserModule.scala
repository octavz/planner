package org.planner.modules.core

import org.planner.dal.{Oauth2DAL, UserDAL}
import org.planner.modules.Result
import org.planner.modules.dto.UserDTO

import scalaoauth2.provider.{AuthorizationRequest, GrantHandlerResult, OAuthError}

/**
 * Created by Octav on 6/30/2014.
 */
trait UserModule {
  val dal: UserDAL
  val dalAuth: Oauth2DAL

  def getUserById(uid: String): Result[UserDTO]

  def createSession(accessToken: String): Result[String]

  def login(request: AuthorizationRequest): Either[OAuthError, GrantHandlerResult]

  def registerUser(u: UserDTO): Result[UserDTO]
}
