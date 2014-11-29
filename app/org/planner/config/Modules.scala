package org.planner.config

import org.planner.controllers.{ProjectController, UserController}

import org.planner.modules.core._
import org.planner.dal._
import org.planner.modules.core.impl._
import org.planner.dal.impl._

object Runtime {
 val userController =  new UserController with DefaultUserModuleComponent with SlickUserDALComponent with SlickOauth2DALComponent with RedisCaching
  val projectController = new ProjectController with SlickProjectDALComponent with RedisCaching with DefaultProjectModuleComponent with SlickOauth2DALComponent with SlickUserDALComponent
}

