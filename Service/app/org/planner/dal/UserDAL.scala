package org.planner.dal

import org.planner.db._

trait UserDAL {

  def create

  def insertSession(us: UserSession): DAO[UserSession]

  def insertUser(user: User): DAO[User]

  def findSessionById(id: String): DAO[Option[UserSession]]

  def deleteSessionByUser(uid: String): DAO[Int]

  def getUserById(uid: String): DAO[User]

  def getUserByEmail(email: String): DAO[Option[User]]

}
