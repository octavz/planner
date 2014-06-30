package org.planner.modules

import org.planner.controllers.{ProjectController, UserController, MainController}
import scaldi.Module
import org.planner.dao._
import org.planner.services._
import org.planner.services.impl._
import org.planner.dao.impl._
import controllers._

class UserModule extends Module {
  bind[UserDAO] to new SlickUserDAO
  bind[UserService] to new DefaultUserService
  bind[Oauth2DAO] to new SlickOauth2DAO

  binding to new UserController
}

class ProjectModule extends Module {
  bind[ProjectDAO] to new SlickProjectDAO
  bind[ProjectService] to new DefaultProjectService

  binding to new ProjectController
}

class MainModule extends Module {

  binding to new MainController
}
