package org.planner.dal

import org.planner.db._
import scala.concurrent._
import scalaoauth2.provider.{ClientCredential, AuthInfo, DataHandler}

trait Oauth2DALComponent {
  val dalAuth: Oauth2DAL

  trait Oauth2DAL extends DataHandler[User] {

    def deleteExistingAndCreate(accessToken: org.planner.db.AccessToken, userId: String, clientId: String): Future[Int]

    def getUserById(id: String): Future[Option[User]]

    def getAccessTokenById(token: String): Future[Option[org.planner.db.AccessToken]]

    def findRefreshToken(token: String): Future[Option[org.planner.db.AccessToken]]

    def findAuthCode(code: String): Future[Option[AuthCode]]

    //  override def findAuthInfoByCode(code: String): Future[Option[AuthInfo[User]]]
    //
    //  override def findClientUser(clientCredential: ClientCredential, scope: Option[String]): Future[Option[User]]
    //
    //  override def validateClient(clientCredential: ClientCredential, grantpe: String): Future[Boolean]

  }

}
