package org.planner.modules.core

import org.planner.dal.{UserDAL, ProjectDAL}
import org.planner.modules.Result
import org.planner.modules.dto.{ProjectListDTO, ProjectDTO}

/**
 * Created by Octav on 6/30/2014.
 */
trait ProjectModule extends BaseModule {
  val dal: ProjectDAL
  val userDal: UserDAL

  def insertProject(project: ProjectDTO): Result[ProjectDTO]

  def getUserProjects(id: String, offset: Int, count: Int) : Result[ProjectListDTO]

}
