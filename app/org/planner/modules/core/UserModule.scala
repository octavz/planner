package org.planner.modules.core

import org.planner.db.User
import org.planner.modules.Result
import org.planner.modules.dto.{UserDTO, GroupDTO, RegisterDTO}
import scala.concurrent._

import scalaoauth2.provider.{AuthInfo, AuthorizationRequest, GrantHandlerResult, OAuthError}

/**
 * Created by Octav on 6/30/2014.
 */
trait UserModuleComponent extends BaseModule {
  val userModule: UserModule

  trait UserModule {

    def getUserById(uid: String): Result[UserDTO]

    def getUserByToken(token: String): Result[UserDTO]

    def createSession(accessToken: String): Result[String]

    def login(request: AuthorizationRequest): Future[Either[OAuthError, GrantHandlerResult]]

    def registerUser(u: RegisterDTO): Result[RegisterDTO]

    def addGroup(dto: GroupDTO): Result[GroupDTO]

    def searchUsers(email: Option[String], nick: Option[String])(implicit authInfo: AuthInfo[User]): Result[List[UserDTO]]

  }

}
