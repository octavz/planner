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
class UserModuleSpec extends Specification with Mockito with Injectable {
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

  def newUser = User(id = guid, login = guid, openidToken = None, created = now,
    updated = now, lastLogin = None, password = guid, nick = guid)

  "User service" should {

    "implement createSession" in {
      val m = module
      val us = UserSession(userId = guid, id = guid)
      m.dal.insertSession(any[UserSession]) returns Future.successful(Right(us))
      m.dal.deleteSessionByUser(us.userId) returns Future.successful(Right(1))
      m.dalAuth.findAuthInfoByAccessToken(any[scalaoauth2.provider.AccessToken]) returns Some(
        scalaoauth2.provider.AuthInfo(user = newUser.copy(id = us.userId), clientId = "1", scope = None, redirectUri = None))
      val s = Await.result(m.createSession(us.userId), duration)
      s must beRight
      there was one(m.dal).insertSession(any[UserSession])
      s.errCode === 0
      s.value.get === us.id
    }

    "implement get user by id and call dal" in {
      val m = module
      val id = guid
      m.dal.getUserById(id) returns Future.successful(Right(newUser.copy(id = id)))
      val s = Await.result(m.getUserById(id), duration)
      there was one(m.dal).getUserById(id)
    }

    "implement register and call dal" in {
      val service = module
      val u = UserDTO(login = guid, password = guid)
      service.dal.insertUser(any[User]) answers (a => dao(a.asInstanceOf[User]))
      service.dal.getUserByEmail(any[String]) returns dao(None)
      val s = Await.result(service.registerUser(u), duration)
      there was one(service.dal).getUserByEmail(anyString)
      there was one(service.dal).insertUser(any[User])
      s must beRight
    }

  }
}
