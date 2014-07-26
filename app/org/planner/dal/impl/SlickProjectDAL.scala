package org.planner.dal.impl

import org.planner.dal._
import org.planner.db._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import scaldi.Injector
import scaldi.Injectable._

import scala.concurrent._

class SlickProjectDAL(implicit inj: Injector, app: play.api.Application) extends ProjectDAL with DB {
  val cache = inject[Caching]

  def insertProject(model: Project, group: Group): DAL[Project] =
    DB.withTransaction {
      implicit session =>
        Projects.insert(model)
        Groups.insert(group)
        GroupsUsers.insert(GroupsUser(userId = model.userId, groupId = group.id))
        dal(model)
    }

  override def getUserProjects(uid: String): DAL[List[(Group,Project)]] =
    DB.withSession {
      implicit session =>
        val q = for { 
          g <- Groups
          gu <- GroupsUsers if g.id === gu.groupId && gu.userId === uid
          p <- Projects if g.projectId === p.id
        } yield (g,p)
        val ret = q.list()
        dal(ret)
    }

  override def getProjectGroupIds(projectId: String): DAL[List[String]] =
    DB.withSession {
      implicit session =>
        val ret = Groups.where(_.projectId === projectId).list()
        dal(ret map (_.id))
    }
}
