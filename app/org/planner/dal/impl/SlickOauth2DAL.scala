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
import ExecutionContext.Implicits.global

class SlickOauth2DAL extends Oauth2DAL with DB {

  override def validateClient(clientId: String, clientSecret: String, grantType: String): Future[Boolean] = Future.successful {
    DB.withTransaction { implicit session =>
      val check = for {
        ((c, cgt), gt) <- Clients innerJoin ClientGrantTypes on (_.id === _.clientId) innerJoin GrantTypes on (_._2.grantTypeId === _.id)
        if c.id === clientId && c.secret === clientSecret && gt.grantType === grantType
      } yield 0
      check.firstOption.isDefined
    }
  }

  override def findUser(username: String, password: String): Future[Option[User]] = Future.successful {
    DB.withTransaction { implicit session =>
      Users.filter(u => u.login === username && u.password === password).firstOption
    }
  }

  override def createAccessToken(authInfo: AuthInfo[User]): Future[scalaoauth2.provider.AccessToken] = {
    val accessTokenExpiresIn = 60 * 60 // 1 hour
    val now = new Date()
    val createdAt = new Timestamp(now.getTime)
    val refreshToken = Some(Crypto.generateToken())
    val accessToken = Crypto.generateToken()

    val tokenObject = AccessToken(accessToken, refreshToken, authInfo.user.id, authInfo.scope, accessTokenExpiresIn, createdAt, authInfo.clientId)
    deleteExistingAndCreate(tokenObject, authInfo.user.id, authInfo.clientId) map { _ =>
      scalaoauth2.provider.AccessToken(accessToken, refreshToken, authInfo.scope, Some(accessTokenExpiresIn.toLong), now)
    }
  }

  override def deleteExistingAndCreate(accessToken: AccessToken, userId: String, clientId: String): Future[Int] = Future.successful {
    DB.withTransaction {
      implicit session =>
        // these two operations should happen inside a transaction
        AccessTokens.filter(a => a.clientId === clientId && a.userId === userId).delete
        AccessTokens.insert(accessToken)
    }
  }

  override def getStoredAccessToken(authInfo: AuthInfo[User]): Future[Option[scalaoauth2.provider.AccessToken]] = Future.successful {
    DB.withTransaction {
      implicit session =>
        AccessTokens.filter(a => a.clientId === authInfo.clientId && a.userId === authInfo.user.id).firstOption map { a =>
          scalaoauth2.provider.AccessToken(a.accessToken, a.refreshToken, a.scope, Some(a.expiresIn.toLong), a.created)
        }
    }
  }

  override def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): Future[scalaoauth2.provider.AccessToken] = {
    createAccessToken(authInfo)
  }

  override def findClientUser(clientId: String, clientSecret: String, scope: Option[String]): Future[Option[User]] = Future.successful {
    None // Not implemented yet
  }

  override def findAccessToken(token: String): Future[Option[scalaoauth2.provider.AccessToken]] = Future.successful {
    DB.withTransaction {
      implicit session =>
        AccessTokens.filter(a => a.accessToken === token).firstOption map { a =>
          scalaoauth2.provider.AccessToken(a.accessToken, a.refreshToken, a.scope, Some(a.expiresIn.toLong), a.created)
        }
    }
  }

  override def getUserById(id: String): Future[Option[User]] = Future.successful {
    DB.withTransaction { implicit session =>
      Users.filter(u => u.id === id).firstOption
    }
  }

  override def getAccessTokenById(token: String): Future[Option[AccessToken]] = Future.successful {
    DB.withTransaction { implicit session =>
      AccessTokens.filter(u => u.accessToken === token).firstOption
    }
  }

  override def findRefreshToken(token: String): Future[Option[AccessToken]] = Future.successful {
    DB.withTransaction { implicit session =>
      AccessTokens.filter(a => a.refreshToken === token).firstOption
    }
  }

  override def findAuthInfoByAccessToken(accessToken: scalaoauth2.provider.AccessToken): Future[Option[AuthInfo[User]]] =
    getAccessTokenById(accessToken.token) flatMap {
      case Some(a) => getUserById(a.userId) map {
        _.map(user => AuthInfo(user, a.clientId, a.scope, Some("")))
      }
      case _ => Future.successful(None)
    }

  override def findAuthInfoByRefreshToken(refreshToken: String): Future[Option[AuthInfo[User]]] =
    findRefreshToken(refreshToken) flatMap {
      case Some(a) => getUserById(a.userId) map {
        _.map { user => AuthInfo(user, a.clientId, a.scope, Some(""))}
      }
      case _ => Future.successful(None)
    }

  override def findAuthCode(code: String): Future[Option[AuthCode]] = Future.successful {
    DB.withTransaction { implicit session =>
      val authCode = AuthCodes.filter(a => a.authorizationCode === code).firstOption
      // filtering out expired authorization codes
      authCode.filter(p => p.createdAt.getTime + (p.expiresIn * 1000) > new Date().getTime)
    }
  }

  override def findAuthInfoByCode(code: String): Future[Option[AuthInfo[User]]] =
    findAuthCode(code) flatMap {
      case Some(code) =>
        getUserById(code.userId) map {
          u => Some(AuthInfo(u.get, code.clientId, code.scope, code.redirectUri))
        }
      case _ => Future.successful(None)
    }
}
