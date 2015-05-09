package org.planner.dal.impl

import org.planner.dal.{DAL}
import scala.concurrent._
import play.api.db.slick.Config.driver.simple._
import play.api.Play.current
import org.planner.db._
import play.api.db.slick.DB
import org.planner.dal._
import scala.concurrent._
import ExecutionContext.Implicits.global

trait SlickUserDALComponent extends UserDALComponent with DB with ModelJson {
  this: Caching =>
  val dalUser = new SlickUserDAL()

  class SlickUserDAL extends UserDAL {

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
        getOrElseSync(CacheKeys.session(id)) {
          UserSessions.filter(_.id === id).firstOption
        } flatMap {
          case v@Some(_) => dal(v)
          case _ => dalErr("Not found")
        }
    }

    override def deleteSessionByUser(uid: String): DAL[Int] = DB.withSession {
      implicit session =>
        dal(UserSessions.filter(_.userId === uid).delete)
    }

    override def getUserById(uid: String): DAL[User] = DB.withSession {
      implicit session =>
        getOrElseSync[Option[User]](CacheKeys.user(uid)) {
          Users.filter(_.id === uid).firstOption
        } flatMap {
          case Some(v) => dal(v)
          case _ => dalErr("Not found")
        }
    }

    override def insertUser(user: User): DAL[User] = DB.withSession {
      implicit session =>
        Users.insert(user)
        dal(user)
    }

    override def getUserByEmail(email: String): DAL[Option[User]] = DB.withSession {
      implicit session =>
        val ret = Users.filter(_.login === email).firstOption
        dal(ret)
    }

    override def insertGroup(model: Group): DAL[Group] = DB.withSession {
      implicit session =>
        Groups.insert(model)
        dal(model)
    }

    override def insertGroupsUser(model: GroupsUser): DAL[GroupsUser] = DB.withSession {
      implicit session =>
        GroupsUsers.insert(model)
        dal(model)
    }

    override def insertGroupWithUser(model: Group, userId: String): DAL[Group] = DB.withTransaction {
      implicit session =>
        Groups.insert(model)
        GroupsUsers.insert(GroupsUser(model.id, userId))
        dal(model)
    }

    override def getUserGroupsIds(userId: String): DAL[List[String]] = DB.withSession {
      implicit session =>
        getOrElse[List[String]](CacheKeys.userGroupsIds(userId)) {
          dal(GroupsUsers.filter(_.userId === userId).list map (_.groupId))
        }
    }

    override def getUserGroups(userId: String): DAL[List[Group]] = DB.withSession {
      implicit session =>
        getOrElse[List[Group]](CacheKeys.userGroups(userId)) {
          val q = for {
            (groupUser, group) <- GroupsUsers innerJoin Groups on (_.groupId === _.id)
          } yield group
          dal(q.list)
        }
    }

    override def searchUsers(email: Option[String], nick: Option[String]): DAL[List[User]] = DB.withSession {
      implicit session =>
        val ret = (if (email.isDefined && nick.isDefined)
          Users.filter(u => u.nick === nick.get || u.login === email.get).list
        else if (email.isDefined && nick.isEmpty)
          Users.filter(u => u.login === email.get).list
        else if (email.isEmpty && nick.isDefined)
          Users.filter(u => u.nick === nick.get).list
        else List.empty)
        dal(ret)
    }
  }

}
