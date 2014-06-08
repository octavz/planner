package services.impl

import util.Gen._

import scala.concurrent._
import ExecutionContext.Implicits._
import services._
import repos._
import db._
import scaldi.Injector
import scaldi.Injectable._
import play.api.Logger

class DefaultUserService(implicit inj: Injector) extends UserService {
    val repo = inject [UserRepo]

    override def createSession(uid: String): RetService[String] = {
      try {
        val model = UserSession(userId = uid, id = guid)
        repo.insertSession(model) map {
          case Right(m) => Right(m.id)
          case Left(err) => Left(1, err)
        }
      }catch{
        case e: Throwable =>
          Logger.error("createSession", e)
          Future.successful(Left(2, e.getMessage))
      }
    }
}
