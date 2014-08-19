package org.planner.dal.impl

import org.planner.dal._
import org.planner.db._
import org.planner.modules.PermProject
import play.api.db.slick.Config.driver.simple._
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation
import play.api.db.slick.DB
import scaldi.Injector
import scaldi.Injectable._

class SlickProjectDAL(implicit inj: Injector, app: play.api.Application) extends ProjectDAL with DB {
  val cache = inject[Caching]

  override def insertProject(model: Project, group: Group): DAL[Project] =
    DB.withTransaction {
      implicit session =>
        Projects.insert(model)
        Groups.insert(group)
        GroupsUsers.insert(GroupsUser(userId = model.userId, groupId = group.id))
        dal(model)
    }

  override def updateProject(model: Project): DAL[Project] =
    DB.withTransaction {
      implicit session =>
        dal(model)
    }

  override def getUserProjects(uid: String, offset: Int, count: Int): DAL[List[(Group,Project)]] =
    DB.withSession {
      implicit session =>
        val q = for { 
          g <- Groups
          gu <- GroupsUsers if g.id === gu.groupId && gu.userId === uid
          p <- Projects if g.projectId === p.id
        } yield (g,p)
        val ret = q.list
        dal(ret)
    }

  override def getProjectGroupIds(projectId: String): DAL[List[String]] =
    DB.withSession {
      implicit session =>
        val ret = Groups.where(_.projectId === projectId).list
        dal(ret map (_.id))
    }

  override def getUserPublicProjects(
    uid: String, offset: Int, count: Int): DAL[List[(Group, Project)]] = 
    DB.withSession {
      implicit session =>
        val projectsByUser = sql"""
            SELECT g.*, p.* FROM projects p
            INNER JOIN groups g ON g.project_id = p.id
            INNER JOIN groups_users gu ON gu.group_id = g.id
            WHERE
            gu.user_id = $uid AND
            (p.perm & 64 <> 0 OR p.perm & 128 <> 0)
            offset $offset limit $count
            """.as[(Group, Project)]
        val ret = projectsByUser.list
        dal(ret)
  }

  override def getProjectById(id: String) = DB.withSession {
    implicit session =>
      dal(Projects.where(_.id === id).firstOption)
  }

}
