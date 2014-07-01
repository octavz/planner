import org.planner.modules.core.UserModule
import org.planner.modules.dto.UserDTO
import scaldi.{Module, Injectable}
import org.planner.util.Gen._
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import scala.concurrent._
import scala.concurrent.duration._
import org.specs2.mock._

import org.planner.modules._
import org.planner.modules.core.impl._
import org.planner.dal._
import org.planner.db._
import org.planner.util.Time._

/** .
  * main test class for DefaultAssetServiceComponent
  * it mocks AssetRepoComponent
  */
@RunWith(classOf[JUnitRunner])
class UserSpec extends Specification with Mockito with Injectable {
  implicit val modules = new Module {
    bind[UserDAL] toProvider mock[UserDAL]
    bind[Oauth2DAL] toProvider mock[Oauth2DAL]
    bind[UserModule] toProvider new DefaultUserModule
  }

  val duration = Duration.Inf

  def module = inject[UserModule]

  /**
   * generates  strings to be used in test
   * @param size the size of the string
   * @return the generated string
   */
  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  "User service" should {

    "implement createSession" in {
      val m = module
      val us = UserSession(userId = guid, id = guid)
      m.repo.insertSession(any[UserSession]) returns Future.successful(Right(us))
      m.repo.deleteSessionByUser(us.userId) returns Future.successful(Right(1))
      m.repoAuth
        .findAuthInfoByAccessToken(any[scalaoauth2.provider.AccessToken]) returns
        Some(scalaoauth2.provider.AuthInfo(
          user = User(id = us.userId, login = guid, openidToken = None
            , created = now, updated = now, lastLogin = None, password = guid)
          , clientId = "1", scope = None, redirectUri = None))
      val s = Await.result(m.createSession(us.userId), duration)
      there was one(m.repo).insertSession(any[UserSession])
      s.errCode === 0
      s.value.get === us.id
    }

    "implement get user by id and call org.planner.dao" in {
      val m = module
      val id = guid
      m.repo.getUserById(id) returns Future.successful(Right(User(
        id = id, login = "test@email.com", openidToken = None
        , created = now, updated = now, lastLogin = None, password = "")))
      val s = Await.result(m.getUserById(id), duration)
      there was one(m.repo).getUserById(id)
    }

    "implement register and call org.planner.dao" in {
      val service = module
      val u = UserDTO(login = guid, password = guid)
      service.repo.insertUser(any[User]) answers (a => dao(a.asInstanceOf[User]))
      service.repo.getUserByEmail(any[String]) returns dao(None)
      val s = Await.result(service.registerUser(u), duration)
      there was one(service.repo).getUserByEmail(anyString)
      there was one(service.repo).insertUser(any[User])
      s must beRight
    }

  }
}


