package org.planner.dal.impl

import java.sql.Timestamp
import java.util.Date

import org.planner.dal.Oauth2DALComponent
import org.planner.db._
import org.planner.util.Crypto
import play.api.Play
import play.api.db.slick.{HasDatabaseConfig, DatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scalaoauth2.provider.{AuthInfo, ClientCredential}

trait SlickOauth2DALComponent extends Oauth2DALComponent with DB with HasDatabaseConfig[JdbcProfile] {
  val dalAuth = new SlickOauth2DAL
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  //  import slick.driver.PostgresDriver.api._

  class SlickOauth2DAL extends Oauth2DAL {

    override def validateClient(clientCredential: ClientCredential, grantType: String): Future[Boolean] = {
      val check = for {
        ((c, cgt), gt) <- Clients join ClientGrantTypes on (_.id === _.clientId) join GrantTypes on (_._2.grantTypeId === _.id)
        if c.id === clientCredential.clientId && c.secret === clientCredential.clientSecret && gt.grantType === grantType
      } yield 0

      db.run(check.result) map (_.nonEmpty)
    }

    override def findUser(username: String, password: String): Future[Option[User]] = {
      val action = Users.filter(u => u.login === username && u.password === password).result.headOption
      db run action
    }

    override def createAccessToken(authInfo: AuthInfo[User]): Future[scalaoauth2.provider.AccessToken] = {
      val accessTokenExpiresIn = 60 * 60 // 1 hour
      val now = new Date()
      val createdAt = new Timestamp(now.getTime)
      val refreshToken = Some(Crypto.generateToken())
      val accessToken = Crypto.generateToken()

      val tokenObject = AccessToken(accessToken, refreshToken, authInfo.user.id, authInfo.scope, accessTokenExpiresIn, createdAt, authInfo.clientId.get)
      deleteExistingAndCreate(tokenObject, authInfo.user.id, authInfo.clientId.get) map { _ =>
        scalaoauth2.provider.AccessToken(accessToken, refreshToken, authInfo.scope, Some(accessTokenExpiresIn.toLong), now)
      }
    }

    override def deleteExistingAndCreate(accessToken: AccessToken, userId: String, clientId: String): Future[Int] = {
      val action = (for {
      // these two operations should happen inside a transaction
        d <- AccessTokens.filter(a => a.clientId === clientId && a.userId === userId).delete
        r <- AccessTokens += accessToken
      } yield r).transactionally

      db run action
    }

    override def getStoredAccessToken(authInfo: AuthInfo[User]): Future[Option[scalaoauth2.provider.AccessToken]] = {
      val action = AccessTokens.filter(a => a.clientId === authInfo.clientId && a.userId === authInfo.user.id).result.headOption
      db.run(action).map {
        opt =>
          opt.map(a =>
            scalaoauth2.provider.AccessToken(a.accessToken, a.refreshToken, a.scope, Some(a.expiresIn.toLong), a.created))
      }
    }

    override def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): Future[scalaoauth2.provider.AccessToken] = {
      createAccessToken(authInfo)
    }

    override def findAccessToken(token: String): Future[Option[scalaoauth2.provider.AccessToken]] = {
      val action = AccessTokens.filter(_.accessToken === token).result.headOption
      db.run(action).map {
        opt =>
          opt.map(a =>
            scalaoauth2.provider.AccessToken(a.accessToken, a.refreshToken, a.scope, Some(a.expiresIn.toLong), a.created))
      }
    }

    override def getUserById(id: String): Future[Option[User]] = {
      db.run(Users.filter(_.id === id).result.headOption)
    }

    override def getAccessTokenById(token: String): Future[Option[AccessToken]] = {
      db.run(AccessTokens.filter(_.accessToken === token).result.headOption)
    }

    override def findRefreshToken(token: String): Future[Option[AccessToken]] = {
      db.run(AccessTokens.filter(_.refreshToken === token).result.headOption)
    }

    override def findAuthInfoByAccessToken(accessToken: scalaoauth2.provider.AccessToken): Future[Option[AuthInfo[User]]] =
      getAccessTokenById(accessToken.token) flatMap {
        case Some(a) => getUserById(a.userId) map {
          _.map(user => AuthInfo(user, Some(a.clientId), a.scope, Some("")))
        }
        case _ => Future.successful(None)
      }

    override def findAuthInfoByRefreshToken(refreshToken: String): Future[Option[AuthInfo[User]]] =
      findRefreshToken(refreshToken) flatMap {
        case Some(a) => getUserById(a.userId) map {
          _.map { user => AuthInfo(user, Some(a.clientId), a.scope, Some("")) }
        }
        case _ => Future.successful(None)
      }

    override def findAuthCode(code: String): Future[Option[AuthCode]] = {
      db.run(AuthCodes.filter(a => a.authorizationCode === code).result.headOption).map {
        authCode =>
          authCode.filter(p => p.createdAt.getTime + (p.expiresIn * 1000) > new Date().getTime)
      }
    }

    override def findAuthInfoByCode(code: String): Future[Option[AuthInfo[User]]] =
      findAuthCode(code) flatMap {
        case Some(c) =>
          getUserById(c.userId) map {
            u => Some(AuthInfo(u.get, Some(c.clientId), c.scope, c.redirectUri))
          }
        case _ => Future.successful(None)
      }


    override def findClientUser(clientCredential: ClientCredential, scope: Option[String]): Future[Option[User]] = Future.successful {
      //    scope match {
      //      case Some(s) => Clients.filter(c => c.id === clientCredential.clientId && c.scope === s)
      //      case _ => Clients.filter(c => c.id === clientCredential.clientId )
      //    }
      None
    }

    override def deleteAuthCode(code: String): Future[Unit] = {
      val action = AuthCodes.filter(_.authorizationCode === code).delete
      db.run(action) map (x => _)
    }
  }

}
