package org.planner.modules.core

import org.planner.modules.Result
import org.planner.modules.dto._

/**
 * Created by Octav on 6/30/2014.
 */
trait ProjectModuleComponent extends BaseModule {
  val projectModule: ProjectModule

  trait ProjectModule {

    def insertProject(project: ProjectDTO): Result[ProjectDTO]

    def updateProject(project: ProjectDTO): Result[ProjectDTO]

    def deleteProject(projectId: String): Result[BooleanDTO]

    def getUserProjects(id: String, offset: Int, count: Int): Result[ProjectListDTO]

    def insertTask(task: TaskDTO): Result[TaskDTO]

    def getTasks(projectId: String, offset: Int, count: Int): Result[TaskListDTO]

  }

}
