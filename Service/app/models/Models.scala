package models

import play.api.db.slick.Config.driver.simple._
import play.api.Play.current
import java.util.Date
import db._
import play.api.db.slick.DB


private[models] trait DAO extends DB {

  def create =
    DB.withSession {
      implicit session =>
      (Clients.ddl ++ Users.ddl ++ GrantTypes.ddl ++ ClientGrantTypes.ddl ++ AccessTokens.ddl ++ AuthCodes.ddl).create
    }
}


object Clients extends DAO {
  def validate(id: String, secret: String, grantType: String): Boolean = {
    DB.withTransaction { implicit session =>
      val check = for {
        ((c, cgt), gt) <- Clients innerJoin ClientGrantTypes on (_.id === _.clientId) innerJoin GrantTypes on (_._2.grantTypeId === _.id)
        if c.id === id && c.secret === secret && gt.grantType === grantType
      } yield 0
      check.firstOption.isDefined
    }
  }

  def findById(id: String): Option[Client] = {
    DB.withTransaction { implicit session =>
      Clients.filter(c => c.id === id).firstOption
    }
  }
}

object Users extends DAO {
  def findUser(username: String, password: String): Option[User] = {
    DB.withTransaction { implicit session =>
      Users.where(u => u.login === username && u.password === password).firstOption
    }
  }

  def getById(id: String): Option[User] = {
    DB.withTransaction { implicit session =>
      Users.where(u => u.id === id).firstOption
    }
  }
}

object AccessTokens extends DAO {
  def create(accessToken: AccessToken) = {
    DB.withTransaction { implicit session =>
      AccessTokens.insert(accessToken)
    }
  }

  def deleteExistingAndCreate(accessToken: AccessToken, userId: String, clientId: String) = {
    DB.withTransaction { implicit session =>
      // these two operations should happen inside a transaction
      AccessTokens.where(a => a.clientId === clientId && a.userId === userId).delete
      AccessTokens.insert(accessToken)
    }
  }

  def findToken(userId: String, clientId: String): Option[AccessToken] = {
    DB.withTransaction { implicit session =>
      AccessTokens.where(a => a.clientId === clientId && a.userId === userId).firstOption
    }
  }

  def findAccessToken(token: String): Option[AccessToken] = {
    DB.withTransaction { implicit session =>
      AccessTokens.where(a => a.accessToken === token).firstOption
    }
  }

  def findRefreshToken(token: String): Option[AccessToken] = {
    DB.withTransaction { implicit session =>
      AccessTokens.where(a => a.refreshToken === token).firstOption
    }
  }
}


object AuthCodes extends DAO {
  def find(code: String) = {
    DB.withTransaction { implicit session =>
      val authCode = AuthCodes.where(a => a.authorizationCode === code).firstOption

      // filtering out expired authorization codes
      authCode.filter(p => p.createdAt.getTime + (p.expiresIn * 1000) > new Date().getTime)
    }
  }
}