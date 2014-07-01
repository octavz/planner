package org.planner.modules.core.impl

import org.planner.modules.core.UserModule
import org.planner.modules.dto.UserDTO
import org.planner.util.Gen._

import scala.concurrent._
import ExecutionContext.Implicits._
import org.planner.modules._
import org.planner.dal._
import org.planner.db._
import scaldi.Injector
import scaldi.Injectable._
import play.api.Logger
import java.util.Date

import scalaoauth2.provider._

class DefaultUserModule(implicit inj: Injector) extends UserModule {
  val repo = inject[UserDAL]
  val repoAuth = inject[Oauth2DAL]

  override def createSession(accessToken: String): Result[String] = {
    try {
      val authInfo = repoAuth.findAuthInfoByAccessToken(scalaoauth2.provider.AccessToken(accessToken, None, None, None, new Date()))
      if (authInfo.isEmpty) throw new Exception("Access token not found!")
      val model = UserSession(userId = authInfo.get.user.id, id = accessToken)
      repo.deleteSessionByUser(model.userId) flatMap {
        case Right(_) =>
          repo.insertSession(model) map {
            case Right(m) => Right(m.id)
            case Left(err) => Left(1, err)
          }
        case _ => Future.successful(Left(2, "Cannot delete existing session"))
      }
    } catch {
      case e: Throwable =>
        Logger.error("createSession", e)
        Future.successful(Left(2, e.getMessage))
    }
  }

  override def login(request: AuthorizationRequest) = {
    val ret = TokenEndpoint.handleRequest(request, repoAuth)
    ret match {
      case Right(v) =>
        createSession(v.accessToken)
        ret
      case _ => ret
    }
  }

  override def getUserById(id: String) = {
    try {
      repo.getUserById(id) map {
        case Right(u) => Right(new UserDTO(u))
        case Left(err) => Left(404, err)
      } recover {
        case e: Throwable => Left(501, e.getMessage)
      }
    } catch {
      case e: Throwable => resultEx(e, "getUserById")
    }
  }

  def registerUser(u: UserDTO): Result[UserDTO] = {
    try {
      repo.getUserByEmail(u.login) flatMap {
        case Right(o) =>
          if (o.isDefined) resultError(400, "User already exists")
          else {
            val model = u.toModel
            repo.insertUser(model) map (_ => Right(new UserDTO(model)))
          }
        case Left(err) => resultError(400, err)
      }
    } catch {
      case e: Throwable => resultEx(e, "registerUser")
    }

  }


}
