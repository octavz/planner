package repos.impl

import repos.{RetRepo, UserRepo}
import db.UserSession

class SlickUserRepo extends UserRepo {
  override def insertSession(us: UserSession): RetRepo[UserSession] = ???
}
