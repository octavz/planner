import org.planner.modules.dto.{GroupDTO, UserDTO}
import play.api.http.Status
import org.planner.util.Gen._
import org.planner.util.Time._
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

import scalaoauth2.provider.AuthInfo

/** .
  * main test class for DefaultAssetServiceComponent
  * it mocks AssetRepoComponent
  */
@RunWith(classOf[JUnitRunner])
class UserModuleSpec extends Specification with Mockito {

  val duration = Duration.Inf

  def module = {
    val ret = new DefaultUserModuleComponent with UserDALComponent with Oauth2DALComponent{
      val dalUser = mock[UserDAL]
      val dalAuth = mock[Oauth2DAL]

    }
    ret.authData = AuthInfo[User](user =
      User(id = guid, login = guid, password = guid, created = now, updated = now, userId = None, groupId = None,
        lastLogin = nowo, providerToken = guido, nick = guid), Some("1"), None, None)
    ret
  }

  /**
   * generates  strings to be used in test
   * @param size the size of the string
   * @return the generated string
   */
  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  def newUser = User(id = guid, login = guid, providerToken = None, created = now, userId = None, groupId = None, updated = now, lastLogin = None, password = guid, nick = guid)

  "User module" should {

    "implement createSession" in {
      val m = module
      val us = UserSession(userId = guid, id = guid)

      m.dalUser.insertSession(any[UserSession]) returns Future.successful(us)
      m.dalUser.deleteSessionByUser(us.userId) returns Future.successful(1)
      m.dalAuth.findAuthInfoByAccessToken(any[scalaoauth2.provider.AccessToken]) returns Future.successful(Some(
        AuthInfo(user = newUser.copy(id = us.userId), clientId = Some("1"), scope = None, redirectUri = None)))

      val s = Await.result(m.userModule.createSession(us.id), duration)
      there was one(m.dalUser).insertSession(any[UserSession])
      s must beRight
      s.errCode === 0
      s.value.get === us.id
    }

    "implement get user by id and call dal" in {
      val m = module
      val id = guid
      m.dalUser.getUserById(id) returns Future.successful(newUser.copy(id = id))
      val s = Await.result(m.userModule.getUserById(id), duration)
      there was one(m.dalUser).getUserById(id)
    }

    "implement register and call dal" in {
      val service = module
      val u = UserDTO(login = guid, password = guid)
      service.dalUser.insertUser(any[User]) answers (a => dal(a.asInstanceOf[User]))
      service.dalUser.getUserByEmail(any[String]) returns dal(None)
      val s = Await.result(service.userModule.registerUser(u), duration)
      there was one(service.dalUser).getUserByEmail(anyString)
      there was one(service.dalUser).insertUser(any[User])
      s must beRight
    }

    "not call insert if email already exists" in {
      val service = module
      val u = UserDTO(login = guid, password = guid)
      service.dalUser.insertUser(any[User]) answers (a => dal(a.asInstanceOf[User]))
      service.dalUser.getUserByEmail(any[String]) returns dal(Some(newUser))

      val s = Await.result(service.userModule.registerUser(u), duration)

      there was one(service.dalUser).getUserByEmail(anyString)
      there was no(service.dalUser).insertUser(any[User])
      s must beLeft
      s.errCode === 500
      s.errMessage must contain("Email already exists")
    }

    "implement add group and call dal" in {
      val service = module
      val dto = GroupDTO(id = None, name = guid, projectId = guid)

      service.dalUser.insertGroupWithUser(any, any) answers (a => dal(a.asInstanceOf[Group]))

      val s = Await.result(service.userModule.addGroup(dto), duration)

      there was one(service.dalUser).insertGroupWithUser(any, any)
      s must beRight
    }

    "handle add group dal error" in {
      val service = module
      val u = GroupDTO(id = None, name = guid, projectId = guid)
      service.dalUser.insertGroupWithUser(any, any) returns dalErr("dal test error")
      val s = Await.result(service.userModule.addGroup(u), duration)
      there was one(service.dalUser).insertGroupWithUser(any, any)
      s must beLeft
      val (code, msg) = s.merge
      code === Status.INTERNAL_SERVER_ERROR
      msg === "dal test error"
    }

    "handle add group future error" in {
      val service = module
      val u = GroupDTO(id = None, name = guid, projectId = guid)
      service.dalUser.insertGroupWithUser(any[Group], any) returns Future.failed(new Exception("test"))
      val s = Await.result(service.userModule.addGroup(u), duration)
      there was one(service.dalUser).insertGroupWithUser(any, any)
      s must beLeft
      val (code, msg) = s.merge
      code === Status.INTERNAL_SERVER_ERROR
      msg === "test"
    }

    "add current user to group upon group creation" in {
      val service = module
      val u = GroupDTO(id = None, name = guid, projectId = guid)
      service.dalUser.insertGroupWithUser(any, any) answers (a => dal(a.asInstanceOf[Group]))

      val s = Await.result(service.userModule.addGroup(u), duration)
      there was one(service.dalUser).insertGroupWithUser(any[Group], any)
      s must beRight
    }

    "work wih chaining" in {

      val f1: Future[Either[String, Int]] = Future.successful(Right(1))

      val res = f1 >>= (v0 => Future.successful(Right((v0, "test")))) >>= { case (a, b) => Future.successful(Right(s"$b$a"))}
      Await.result(res, Duration.Inf) === Right("test1")
    }
  }
}
