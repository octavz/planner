package org.planner.config

import com.google.inject.AbstractModule
import org.planner.controllers.{ProjectController, UserController}

import org.planner.modules.core._
import org.planner.dal._
import org.planner.modules.core.impl._
import org.planner.dal.impl._

class PlannerRunModule extends AbstractModule {
  def configure() = {
    bind(classOf[Oauth2DAL]).to(classOf[SlickOauth2DAL])
    bind(classOf[ProjectDAL]).to(classOf[SlickProjectDAL])
    bind(classOf[UserDAL]).to(classOf[SlickUserDAL])
    bind(classOf[ProjectModule]).to(classOf[DefaultProjectModule])
    bind(classOf[UserModule]).to(classOf[DefaultUserModule])
    bind(classOf[Caching]).to(classOf[RedisCaching])

  }

}
