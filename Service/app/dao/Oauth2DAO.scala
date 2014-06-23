package dao

import db._

import scalaoauth2.provider.{AuthInfo, DataHandler}

trait Oauth2DAO extends DataHandler[User] {

  def validateClient(clientId: String, clientSecret: String, grantType: String): Boolean

  def findUser(username: String, password: String): Option[User]

  def createAccessToken(authInfo: AuthInfo[User]): scalaoauth2.provider.AccessToken

  def deleteExistingAndCreate(accessToken: db.AccessToken, userId: String, clientId: String)

  def getStoredAccessToken(authInfo: AuthInfo[User]): Option[scalaoauth2.provider.AccessToken]

  def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): scalaoauth2.provider.AccessToken

  def findClientUser(clientId: String, clientSecret: String, scope: Option[String]): Option[User]

  def findAccessToken(token: String): Option[scalaoauth2.provider.AccessToken]

  def getUserById(id: String): Option[User]

  def getAccessTokenById(token: String): Option[db.AccessToken]

  def findRefreshToken(token: String): Option[db.AccessToken]

  def findAuthInfoByAccessToken(accessToken: scalaoauth2.provider.AccessToken): Option[AuthInfo[User]]

  def findAuthInfoByRefreshToken(refreshToken: String): Option[AuthInfo[User]]

  def findAuthCode(code: String): Option[AuthCode]

  def findAuthInfoByCode(code: String): Option[AuthInfo[User]]

}
