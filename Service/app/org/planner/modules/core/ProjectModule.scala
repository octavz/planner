package org.planner.modules.core

import org.planner.dal.ProjectDAL
import org.planner.modules.Result
import org.planner.modules.dto.ProjectDTO

/**
 * Created by Octav on 6/30/2014.
 */
trait ProjectModule extends BaseModule {
  val dal: ProjectDAL

  def insertProject(project: ProjectDTO): Result[ProjectDTO]
}
