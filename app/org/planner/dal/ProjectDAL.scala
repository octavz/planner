package org.planner.dal

import org.planner.db._

trait ProjectDALComponent {
  val projectDal: ProjectDAL

  trait ProjectDAL {

    def insertProject(model: Project, group: Group): DAL[Project]

    def getUserProjects(uid: String, offset: Int, count: Int): DAL[List[(Group, Project)]]

    def getUserPublicProjects(uid: String, offset: Int, count: Int): DAL[List[(Group, Project)]]

    def getProjectGroups(projectId: String): DAL[List[Group]]

    def updateProject(project: Project): DAL[Project]

    def getProjectById(id: String): DAL[Option[Project]]

    def insertTask(model: Task): DAL[Task]
  }

}
