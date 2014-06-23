package dao

import db._

trait UserDAO {

  def create

  def insertSession(us: UserSession): RetRepo[UserSession]

  def findSessionById(id: String): RetRepo[Option[UserSession]]

  def deleteSessionByUser(uid: String): RetRepo[Int]

  def getUserById(uid: String): RetRepo[User]

}
