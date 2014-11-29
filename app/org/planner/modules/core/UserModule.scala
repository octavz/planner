package org.planner.modules.core

import org.planner.modules.Result
import org.planner.modules.dto.{GroupDTO, UserDTO}
import scala.concurrent._

import scalaoauth2.provider.{AuthorizationRequest, GrantHandlerResult, OAuthError}

/**
 * Created by Octav on 6/30/2014.
 */
trait UserModuleComponent extends BaseModule {
  val userModule: UserModule

  trait UserModule {

    def getUserById(uid: String): Result[UserDTO]

    def createSession(accessToken: String): Result[String]

    def login(request: AuthorizationRequest): Future[Either[OAuthError, GrantHandlerResult]]

    def registerUser(u: UserDTO): Result[UserDTO]

    def addGroup(dto: GroupDTO): Result[GroupDTO]
  }

}
