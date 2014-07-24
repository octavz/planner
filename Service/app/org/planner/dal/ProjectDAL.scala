package org.planner.dal

import org.planner.db._

trait ProjectDAL {

  def insertProject(model: Project, group: Group): DAL[Project]

  def getUserProjects(uid: String): DAL[List[(Group,Project)]]

  def getProjectGroupIds(projectId: String): DAL[List[String]]

}
