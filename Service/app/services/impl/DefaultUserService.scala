package services.impl

import dto.UserDTO
import util.Gen._

import scala.concurrent._
import ExecutionContext.Implicits._
import services._
import dao._
import db._
import scaldi.Injector
import scaldi.Injectable._
import play.api.Logger
import java.util.Date

import scalaoauth2.provider._

class DefaultUserService(implicit inj: Injector) extends UserService {
  val repo = inject[UserDAO]
  val repoAuth = inject[Oauth2DAO]

  override def createSession(accessToken: String): RetService[String] = {
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
        case e: Throwable => Left(2, e.getMessage)
      }
    } catch {
      case e: Throwable => Future.successful(Left(2, e.getMessage))
    }
  }


}
