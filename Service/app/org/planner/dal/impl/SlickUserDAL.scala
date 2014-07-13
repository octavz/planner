package org.planner.dal.impl

import org.planner.dal.{DAL, UserDAL}
import scaldi.Injector
import scala.concurrent._
import play.api.db.slick.Config.driver.simple._
import play.api.Play.current
import org.planner.db._
import play.api.db.slick.DB
import org.planner.dal._
import scaldi.Injectable._
import scala.concurrent._
import ExecutionContext.Implicits.global

class SlickUserDAL(implicit inj: Injector) extends UserDAL with DB {
  val cache = inject[Caching]

  override def create =
    DB.withSession {
      implicit session =>
        (Clients.ddl ++ Users.ddl ++ GrantTypes.ddl ++ ClientGrantTypes.ddl ++ AccessTokens.ddl ++ AuthCodes.ddl).create
    }

  override def insertSession(us: UserSession): DAL[UserSession] = DB.withSession {
    implicit session =>
      UserSessions.insert(us)
      dal(us)
  }

  override def findSessionById(id: String): DAL[Option[UserSession]] = DB.withSession {
    implicit session =>
      cache.getOrElseSync(CacheKeys.session(id)) {
        UserSessions.where(_.id === id).firstOption
      } map {
        case v@Some(_) => Right(v)
        case _ => Left("Not found")
      }
  }

  override def deleteSessionByUser(uid: String): DAL[Int] = DB.withSession {
    implicit session =>
      dal(UserSessions.where(_.userId === uid).delete)
  }

  override def getUserById(uid: String): DAL[User] = DB.withSession {
    implicit session =>
      cache.getOrElseSync[Option[User]](CacheKeys.user(uid)) {
        Users.where(_.id === uid).firstOption
      } map {
        case Some(v) => Right(v)
        case _ => Left("Not found")
      }
  }

  override def insertUser(user: User): DAL[User] = DB.withSession {
    implicit session =>
      Users.insert(user)
      dal(user)
  }

  override def getUserByEmail(email: String): DAL[Option[User]] = DB.withSession {
    implicit session =>
      val ret = Users.where(_.login === email).firstOption
      dal(ret)
  }

  override def insertGroup(model: Group): DAL[Group] = ???

  override def insertGroupsUser(model: GroupsUser): DAL[GroupsUser] = ???
}
