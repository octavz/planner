package dao

import db._

import scalaoauth2.provider.DataHandler

trait UserDAO extends DataHandler[User] {

  def insertSession(us: UserSession): RetRepo[UserSession]

}
