package org.planner.dao

import org.planner.db._

trait UserDAO {

  def create

  def insertSession(us: UserSession): RetRepo[UserSession]

  def insertUser(user: User): RetRepo[User]

  def findSessionById(id: String): RetRepo[Option[UserSession]]

  def deleteSessionByUser(uid: String): RetRepo[Int]

  def getUserById(uid: String): RetRepo[User]

  def getUserByEmail(email: String): RetRepo[Option[User]]

}
