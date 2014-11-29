package org.planner.modules.core

import org.planner.modules.Result
import org.planner.modules.dto.{ProjectListDTO, ProjectDTO}

/**
 * Created by Octav on 6/30/2014.
 */
trait ProjectModuleComponent extends BaseModule {
  val projectModule: ProjectModule

  trait ProjectModule {

    def insertProject(project: ProjectDTO): Result[ProjectDTO]

    def updateProject(project: ProjectDTO): Result[ProjectDTO]

    def getUserProjects(id: String, offset: Int, count: Int): Result[ProjectListDTO]
  }

}
