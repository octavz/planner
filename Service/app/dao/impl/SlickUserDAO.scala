package dao.impl

import dao.{RetRepo, UserDAO}
import db._
import scalaoauth2.provider.{AuthInfo, DataHandler}
import java.util.Date
import java.sql.Timestamp
import util.Crypto

class SlickUserDAO extends UserDAO with DB {
  override def insertSession(us: UserSession): RetRepo[UserSession] = ???

  def validateClient(clientId: String, clientSecret: String, grantType: String): Boolean = {
    models.Clients.validate(clientId, clientSecret, grantType)
  }

  def findUser(username: String, password: String): Option[User] = {
    models.Users.findUser(username, password)
  }

  def createAccessToken(authInfo: AuthInfo[User]): scalaoauth2.provider.AccessToken = {
    val accessTokenExpiresIn = 60 * 60 // 1 hour
    val now = new Date()
    val createdAt = new Timestamp(now.getTime)
    val refreshToken = Some(Crypto.generateToken())
    val accessToken = Crypto.generateToken()

    val tokenObject = AccessToken(accessToken, refreshToken, authInfo.user.id, authInfo.scope, accessTokenExpiresIn, createdAt, authInfo.clientId)
    models.AccessTokens.deleteExistingAndCreate(tokenObject, authInfo.user.id, authInfo.clientId)
    scalaoauth2.provider.AccessToken(accessToken, refreshToken, authInfo.scope, Some(accessTokenExpiresIn.toLong), now)
  }

  def getStoredAccessToken(authInfo: AuthInfo[User]): Option[scalaoauth2.provider.AccessToken] = {
    models.AccessTokens.findToken(authInfo.user.id, authInfo.clientId) map { a =>
      scalaoauth2.provider.AccessToken(a.accessToken, a.refreshToken, a.scope, Some(a.expiresIn.toLong), a.createdAt)
    }
  }

  def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): scalaoauth2.provider.AccessToken = {
    createAccessToken(authInfo)
  }

  def findClientUser(clientId: String, clientSecret: String, scope: Option[String]): Option[User] = {
    None // Not implemented yet
  }

  def findAccessToken(token: String): Option[scalaoauth2.provider.AccessToken] = {
    models.AccessTokens.findAccessToken(token) map { a =>
      scalaoauth2.provider.AccessToken(a.accessToken, a.refreshToken, a.scope, Some(a.expiresIn.toLong), a.createdAt)
    }
  }

  def findAuthInfoByAccessToken(accessToken: scalaoauth2.provider.AccessToken): Option[AuthInfo[User]] = {
    models.AccessTokens.findAccessToken(accessToken.token) map { a =>
      val user = models.Users.getById(a.userId).get
      AuthInfo(user, a.clientId, a.scope, Some(""))
    }
  }

  def findAuthInfoByRefreshToken(refreshToken: String): Option[AuthInfo[User]] = {
    models.AccessTokens.findRefreshToken(refreshToken) map { a =>
      val user = models.Users.getById(a.userId).get
      AuthInfo(user, a.clientId, a.scope, Some(""))
    }
  }

  def findAuthInfoByCode(code: String): Option[AuthInfo[User]] = {
    models.AuthCodes.find(code) map { a =>
      val user = models.Users.getById(a.userId).get
      AuthInfo(user, a.clientId, a.scope, a.redirectUri)
    }
  }
}
