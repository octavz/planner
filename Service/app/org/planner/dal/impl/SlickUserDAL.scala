package org.planner.dal.impl

import org.planner.dal.{DAO, UserDAL}
import scala.concurrent._
import play.api.db.slick.Config.driver.simple._
import play.api.Play.current
import org.planner.db._
import play.api.db.slick.DB
import org.planner.dal._

class SlickUserDAL extends UserDAL with DB {

  override def create =
    DB.withSession {
      implicit session =>
        (Clients.ddl ++ Users.ddl ++ GrantTypes.ddl ++ ClientGrantTypes.ddl ++ AccessTokens.ddl ++ AuthCodes.ddl).create
    }

  override def insertSession(us: UserSession): DAO[UserSession] = DB.withSession {
    implicit session =>
      UserSessions.insert(us)
      Future.successful(Right(us))
  }

  override def findSessionById(id: String): DAO[Option[UserSession]] = DB.withSession {
    implicit session =>
      val q = for {a <- UserSessions if (a.id === id)} yield a
      val res = q.list()
      Future.successful(Right(if (res.isEmpty) None else Some(res.head)))
  }

  override def deleteSessionByUser(uid: String): DAO[Int] = DB.withSession {
    implicit session =>
      Future.successful(Right(UserSessions.where(_.userId === uid).delete))
  }

  override def getUserById(uid: String): DAO[User] = DB.withSession {
    implicit session =>
      val ret = Users.where(_.id === uid).firstOption
      Future.successful(if (ret.isDefined) Right(ret.get) else Left("Cannot find user"))
  }

  override def insertUser(user: User): DAO[User] = DB.withSession {
    implicit session =>
      Users.insert(user)
      dao(user)
  }

  override def getUserByEmail(email: String): DAO[Option[User]] = DB.withSession {
    implicit session =>
      val ret = Users.where(_.login === email).firstOption
      dao(ret)
  }

}
