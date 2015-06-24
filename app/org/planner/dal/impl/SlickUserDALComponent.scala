package org.planner.dal.impl

import org.planner.dal.{DAL}
import play.api.Play
import play.api.libs.json.{Writes, Reads}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent._
import play.api.Play.current
import org.planner.db._
import play.api.db.slick.{DatabaseConfigProvider}
import org.planner.dal._
import scala.concurrent._
import ExecutionContext.Implicits.global

trait SlickUserDALComponent extends UserDALComponent with DB with ModelJson {
  this: Caching =>
  val dalUser = new SlickUserDAL()

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  val db = dbConfig.db

  import dbConfig.driver.api._

  class SlickUserDAL extends UserDAL {

    override def create = {
      //(Clients.ddl ++ Users.ddl ++ GrantTypes.ddl ++ ClientGrantTypes.ddl ++ AccessTokens.ddl ++ AuthCodes.ddl).create
    }

    override def insertSession(us: UserSession): DAL[UserSession] = {
      db.run(UserSessions += us).map(_ => us)
    }

    override def findSessionById(id: String): DAL[Option[UserSession]] =
      getOrElse(CacheKeys.session(id)) {
        db.run(UserSessions.filter(_.id === id).result.headOption)
      }

    override def deleteSessionByUser(uid: String): DAL[Int] = {
      val action = UserSessions.filter(_.userId === uid).delete
      db.run(action.result)
    }

    override def getUserById(uid: String): DAL[User] = {
      getOrElse(CacheKeys.user(uid)) {
        db.run(Users.filter(_.id === uid).result.head)
      }
    }

    override def insertUser(user: User): DAL[User] = {
      db.run(Users += user).map(_ => user)
    }

    override def getUserByEmail(email: String): DAL[Option[User]] = {
      getOrElse(CacheKeys.byEmail(email)) {
        db.run(Users.filter(_.login === email).result.headOption)
      }
    }

    override def insertGroup(model: Group): DAL[Group] = db.run(Groups.+=(model)) map (_ => model)

    override def insertGroupsUser(model: GroupsUser): DAL[GroupsUser] = db.run(GroupsUsers.+=(model)) map (_ => model)

    override def insertGroupWithUser(model: Group, userId: String): DAL[Group] = {
      val action = (for {
        _ <- Groups += model
        _ <- GroupsUsers += GroupsUser(model.id, userId)
      } yield ()
        ).transactionally
      db run action map (_ => model)
    }

    override def getUserGroupsIds(userId: String): DAL[Seq[String]] =
      getOrElse(CacheKeys.userGroupsIds(userId)) {
        db.run(GroupsUsers.filter(_.userId === userId).map(_.groupId).result)
      }

    override def getUserGroups(userId: String): DAL[List[Group]] = {
      getOrElse(CacheKeys.userGroups(userId)) {
        val q = for {
          (groupUser, group) <- GroupsUsers join Groups on (_.groupId === _.id)
        } yield group
        db.run(q.result).map(_.toList)
      }
    }

    override def searchUsers(email: Option[String], nick: Option[String]): DAL[Seq[User]] = {
      if (email.isDefined && nick.isDefined) db.run(Users.filter(u => u.nick === nick.get || u.login === email.get).result)
      else if (email.isDefined && nick.isEmpty) db.run(Users.filter(u => u.login === email.get).result)
      else if (email.isEmpty && nick.isDefined) db.run(Users.filter(u => u.nick === nick.get).result)
      else Future.successful(Seq.empty)
    }
  }

}
