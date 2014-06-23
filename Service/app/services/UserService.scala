package services

import dao._
import dto._

import scalaoauth2.provider.{GrantHandlerResult, OAuthError, AuthorizationRequest}

trait UserService {
  val repo: UserDAO
  val repoAuth: Oauth2DAO

  def getUserById(uid: String): RetService[UserDTO] = ???

  def createSession(accessToken: String): RetService[String]

  def login(request: AuthorizationRequest): Either[OAuthError, GrantHandlerResult]
}
