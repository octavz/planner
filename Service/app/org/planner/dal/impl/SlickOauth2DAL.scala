package org.planner.dal.impl

import org.planner.dal.{Oauth2DAL, DAL, UserDAL}
import scalaoauth2.provider.{AuthInfo, DataHandler}
import java.sql.Timestamp
import org.planner.util.Crypto
import scala.concurrent._
import play.api.db.slick.Config.driver.simple._
import play.api.Play.current
import java.util.Date
import org.planner.db._
import play.api.db.slick.DB

class SlickOauth2DAL extends Oauth2DAL with DB {

  override def validateClient(clientId: String, clientSecret: String, grantType: String): Boolean =
    DB.withTransaction { implicit session =>
      val check = for {
        ((c, cgt), gt) <- Clients innerJoin ClientGrantTypes on (_.id === _.clientId) innerJoin GrantTypes on (_._2.grantTypeId === _.id)
        if c.id === clientId && c.secret === clientSecret && gt.grantType === grantType
      } yield 0
      check.firstOption.isDefined
    }

  override def findUser(username: String, password: String): Option[User] =
    DB.withTransaction { implicit session =>
      Users.where(u => u.login === username && u.password === password).firstOption
    }

  override def createAccessToken(authInfo: AuthInfo[User]): scalaoauth2.provider.AccessToken = {
    val accessTokenExpiresIn = 60 * 60 // 1 hour
    val now = new Date()
    val createdAt = new Timestamp(now.getTime)
    val refreshToken = Some(Crypto.generateToken())
    val accessToken = Crypto.generateToken()

    val tokenObject = AccessToken(accessToken, refreshToken, authInfo.user.id, authInfo.scope, accessTokenExpiresIn, createdAt, authInfo.clientId)
    deleteExistingAndCreate(tokenObject, authInfo.user.id, authInfo.clientId)
    scalaoauth2.provider.AccessToken(accessToken, refreshToken, authInfo.scope, Some(accessTokenExpiresIn.toLong), now)
  }

  override def deleteExistingAndCreate(accessToken: AccessToken, userId: String, clientId: String) =
    DB.withTransaction { implicit session =>
      // these two operations should happen inside a transaction
      AccessTokens.where(a => a.clientId === clientId && a.userId === userId).delete
      AccessTokens.insert(accessToken)
    }

  override def getStoredAccessToken(authInfo: AuthInfo[User]): Option[scalaoauth2.provider.AccessToken] = DB.withTransaction {
    implicit session =>
      AccessTokens.where(a => a.clientId === authInfo.clientId && a.userId === authInfo.user.id).firstOption map { a =>
        scalaoauth2.provider.AccessToken(a.accessToken, a.refreshToken, a.scope, Some(a.expiresIn.toLong), a.createdAt)
      }
  }

  override def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): scalaoauth2.provider.AccessToken = {
    createAccessToken(authInfo)
  }

  override def findClientUser(clientId: String, clientSecret: String, scope: Option[String]): Option[User] = {
    None // Not implemented yet
  }

  override def findAccessToken(token: String): Option[scalaoauth2.provider.AccessToken] = DB.withTransaction {
    implicit session =>
      AccessTokens.where(a => a.accessToken === token).firstOption map { a =>
        scalaoauth2.provider.AccessToken(a.accessToken, a.refreshToken, a.scope, Some(a.expiresIn.toLong), a.createdAt)
      }
  }

  override def getUserById(id: String): Option[User] = {
    DB.withTransaction { implicit session =>
      Users.where(u => u.id === id).firstOption
    }
  }

  override def getAccessTokenById(token: String): Option[AccessToken] = {
    DB.withTransaction { implicit session =>
      AccessTokens.where(u => u.accessToken === token).firstOption
    }
  }

  override def findRefreshToken(token: String): Option[AccessToken] = {
    DB.withTransaction { implicit session =>
      AccessTokens.where(a => a.refreshToken === token).firstOption
    }
  }

  override def findAuthInfoByAccessToken(accessToken: scalaoauth2.provider.AccessToken): Option[AuthInfo[User]] = {
    getAccessTokenById(accessToken.token) map { a =>
      val user = getUserById(a.userId).get
      AuthInfo(user, a.clientId, a.scope, Some(""))
    }
  }

  override def findAuthInfoByRefreshToken(refreshToken: String): Option[AuthInfo[User]] = {
    findRefreshToken(refreshToken) map { a =>
      val user = getUserById(a.userId).get
      AuthInfo(user, a.clientId, a.scope, Some(""))
    }
  }

  override def findAuthCode(code: String) = {
    DB.withTransaction { implicit session =>
      val authCode = AuthCodes.where(a => a.authorizationCode === code).firstOption
      // filtering out expired authorization codes
      authCode.filter(p => p.createdAt.getTime + (p.expiresIn * 1000) > new Date().getTime)
    }
  }

  override def findAuthInfoByCode(code: String): Option[AuthInfo[User]] = {
    findAuthCode(code) map { a =>
      val user = getUserById(a.userId).get
      AuthInfo(user, a.clientId, a.scope, a.redirectUri)
    }
  }
}
