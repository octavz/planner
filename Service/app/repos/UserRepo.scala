package repos

import db._

trait UserRepo {

  def insertSession(us: UserSession): RetRepo[UserSession]

}
