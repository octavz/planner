package dao.impl

import dao.{RetRepo, UserDAO}
import scalaoauth2.provider.{AuthInfo, DataHandler}
import java.sql.Timestamp
import util.Crypto
import scala.concurrent._
import play.api.db.slick.Config.driver.simple._
import play.api.Play.current
import java.util.Date
import db._
import play.api.db.slick.DB

class SlickUserDAO extends UserDAO with DB {

  override def create =
    DB.withSession {
      implicit session =>
        (Clients.ddl ++ Users.ddl ++ GrantTypes.ddl ++ ClientGrantTypes.ddl ++ AccessTokens.ddl ++ AuthCodes.ddl).create
    }

  override def insertSession(us: UserSession): RetRepo[UserSession] = DB.withSession {
    implicit session =>
      UserSessions.insert(us)
      Future.successful(Right(us))
  }

  override def findSessionById(id: String): RetRepo[Option[UserSession]] = DB.withSession {
    implicit session =>
      val q = for {a <- UserSessions if (a.id === id)} yield a
      val res = q.list()
      Future.successful(Right(if (res.isEmpty) None else Some(res.head)))
  }

  override def deleteSessionByUser(uid: String): RetRepo[Int] = DB.withSession {
    implicit session =>
      Future.successful(Right(UserSessions.where(_.userId === uid).delete))
  }

  override def getUserById(uid: String): RetRepo[User] = DB.withSession {
    implicit session =>
      val ret = Users.where(_.id === uid).firstOption
      Future.successful(if(ret.isDefined) Right(ret.get) else Left("Cannot find user"))
  }
}
