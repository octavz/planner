package org.planner.dal

import org.planner.db._

trait ProjectDAL {


  def insertProject(model: Project, group: Group): DAL[Project]

  def getUserProjects(uid: String, offset: Int, count: Int): DAL[List[(Group, Project)]]

  def getUserPublicProjects(uid: String, offset: Int, count: Int): DAL[List[(Group, Project)]]

  def getProjectGroupIds(projectId: String): DAL[List[String]]

  def updateProject(project: Project): DAL[Project]

  def getProjectById(id: String): DAL[Option[Project]]
}
