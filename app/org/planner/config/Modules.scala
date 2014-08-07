package org.planner.config

import org.planner.controllers.{ProjectController, UserController, MainController}

import org.planner.modules.core._
import scaldi.Module
import org.planner.dal._
import org.planner.modules.core.impl._
import org.planner.dal.impl._
import play.api.Play.current

class UserConf extends Module {
  bind[UserDAL] to new SlickUserDAL
  bind[UserModule] to new DefaultUserModule
  bind[Oauth2DAL] to new SlickOauth2DAL

  binding to new UserController
}

class ProjectConf extends Module {
  bind[ProjectDAL] to new SlickProjectDAL
  bind[ProjectModule] to new DefaultProjectModule

  binding to new ProjectController
}

class MainConf extends Module {
  binding to new MainController
}

class UtilsConf extends Module {
  bind[Caching] to new RedisCaching
}

