package org.planner.modules.core.impl

import org.planner.modules.core.{UserModuleComponent}
import org.planner.modules.dto.{UserDTO, GroupDTO, RegisterDTO}
import play.api.http.Status
import scala.concurrent._
import ExecutionContext.Implicits._
import org.planner.modules._
import org.planner.dal._
import org.planner.db._
import java.util.Date
import scalaoauth2.provider._

trait DefaultUserModuleComponent extends UserModuleComponent {
  this: UserDALComponent with Oauth2DALComponent =>
  val userModule = new DefaultUserModule

  class DefaultUserModule extends UserModule {

    override def createSession(accessToken: String): Result[String] = {
      dalAuth.findAuthInfoByAccessToken(scalaoauth2.provider.AccessToken(accessToken, None, None, None, new Date())) flatMap {
        authInfo =>
          if (authInfo.isEmpty) {
            resultError(Status.NOT_FOUND, "Session not found")
          } else {
            val model = UserSession(userId = authInfo.get.user.id, id = accessToken)
            val f = for {
              fDelete <- dalUser.deleteSessionByUser(model.userId)
              fInsert <- dalUser.insertSession(model)
            } yield resultSync(model.id)

            f recover { case e: Throwable => resultErrorSync(Status.INTERNAL_SERVER_ERROR, e.getMessage)}
          }
      }
    }

    override def login(request: AuthorizationRequest): Future[Either[OAuthError, GrantHandlerResult]] = {
      println(request)
      val ret = TokenEndpoint.handleRequest(request, dalAuth)
      ret flatMap {
        case r@Right(v) =>
          createSession(v.accessToken) map (_ => r)
        case _ => ret
      }
    }

    override def getUserById(id: String): Result[UserDTO] = {
      try {
        dalUser.getUserById(id) map {
          u =>
            resultSync(new UserDTO(u))
        } recover {
          case e: Throwable => resultExSync(e, "getUserById")
        }
      }
       catch {
        case e: Throwable => resultEx(e, "getUserById")
      }
    }

    def registerUser(u: RegisterDTO): Result[RegisterDTO] = {
      try {
        val model = u.toModel

        val f = dalUser.getUserByEmail(u.login) flatMap {
          case Some(_) => resultError(Status.INTERNAL_SERVER_ERROR, "Email already exists")
          case _ => dalUser.insertUser(model) map (a => resultSync(new RegisterDTO(a)))
        }

        f recover { case e: Throwable => resultExSync(e, "registerUser")}
      } catch {
        case e: Throwable => resultEx(e, "registerUser")
      }
    }

    override def addGroup(dto: GroupDTO): Result[GroupDTO] = {
      try {
        val model = dto.toModel(authData.user.id)
        val f = dalUser.insertGroupWithUser(model, authData.user.id) map (_ => resultSync(new GroupDTO(model)))
        f recover { case e: Throwable => resultExSync(e, "addGroup")}
      } catch {
        case e: Throwable =>
          resultEx(e, "addGroup")
      }
    }

    override def getUserByToken(token: String): Result[UserDTO] = {
      val f: Result[UserDTO] = for {
        at <- dalAuth.findAccessToken(token)
        data <- dalAuth.findAuthInfoByAccessToken(at.getOrElse(throw new Exception("Token not found")))
      } yield data match {
          case Some(info) => resultSync(new UserDTO(info.user))
          case _ => resultErrorSync(404, "User not found by access token")
        }

      f recover {
        case e: Throwable => resultExSync(e, "getUserByToken")
      }

    }
  }

}
