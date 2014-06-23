package modules

import scaldi.Module
import dao._
import services._
import services.impl._
import dao.impl._
import controllers._

class UserModule extends Module {
  bind[UserDAO] to new SlickUserDAO
  bind[UserService] to new DefaultUserService
  bind[Oauth2DAO] to new SlickOauth2DAO

  binding to new UserController
}

class MainModule extends Module {

  binding to new MainController
}
