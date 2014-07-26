package org.planner.modules.core.impl

import akka.actor.Status.Success
import org.planner.modules.core.UserModule
import org.planner.modules.dto.{GroupDTO, UserDTO}
import org.planner.util.Gen._
import play.api.http.Status
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
import org.planner.modules.dto.GroupDTO

class DefaultUserModule(implicit inj: Injector) extends UserModule {
  val dal = inject[UserDAL]
  val dalAuth = inject[Oauth2DAL]

  override def createSession(accessToken: String): Result[String] = {
    val authInfo = dalAuth.findAuthInfoByAccessToken(scalaoauth2.provider.AccessToken(accessToken, None, None, None, new Date()))
    if (authInfo.isEmpty) {
      resultError(Status.NOT_FOUND, "Session not found")
    } else {
      val model = UserSession(userId = authInfo.get.user.id, id = accessToken)
      val f = for {
        fDelete <- dal.deleteSessionByUser(model.userId)
        fInsert <- dal.insertSession(model)
      } yield resultSync(model.id)

      f recover { case e: Throwable => resultErrorSync(Status.INTERNAL_SERVER_ERROR, e.getMessage)}
    }
  }

  override def login(request: AuthorizationRequest) = {
    val ret = TokenEndpoint.handleRequest(request, dalAuth)
    ret match {
      case Right(v) =>
        createSession(v.accessToken)
        ret
      case _ => ret
    }
  }

  override def getUserById(id: String) = {
    try {
      dal.getUserById(id) map (u => resultSync(new UserDTO(u))) recover {
        case e: Throwable => resultExSync(e, "getUserById")
      }
    } catch {
      case e: Throwable => resultEx(e, "getUserById")
    }
  }

  def registerUser(u: UserDTO): Result[UserDTO] = {
    try {
      val model = u.toModel

      val f = dal.getUserByEmail(u.login) flatMap {
        case Some(_) => resultError(Status.INTERNAL_SERVER_ERROR, "Email already exists")
        case _ => dal.insertUser(model) map (a => resultSync(new UserDTO(a)))
      }

      f recover { case e: Throwable => resultExSync(e, "registerUser")}
    } catch {
      case e: Throwable => resultEx(e, "registerUser")
    }
  }

  override def addGroup(dto: GroupDTO): Result[GroupDTO] = {
    try {
      val model = dto.toModel(authData.user.id)
      val f = dal.insertGroupWithUser(model, authData.user.id) map (_ => resultSync(new GroupDTO(model)))
      f recover { case e: Throwable => resultExSync(e, "addGroup")}
    } catch {
      case e: Throwable =>
        resultEx(e, "addGroup")
    }
  }
}
