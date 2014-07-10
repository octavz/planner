package org.planner.dal

import org.planner.db._

trait ProjectDAL {

  def insertProject(model: Project): DAL[Project]

  def getUserProjects(uid: String): DAL[List[Project]]

}
