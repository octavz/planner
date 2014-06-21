import scaldi.{Module, Injectable}
import util.Gen._
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import scala.concurrent._
import scala.concurrent.duration._
import org.specs2.mock._

import services._
import services.impl._
import dao._
import db._

/** .
  * main test class for DefaultAssetServiceComponent
  * it mocks AssetRepoComponent
  */
@RunWith(classOf[JUnitRunner])
class UserServiceSpec extends Specification with Mockito with Injectable {
  implicit val modules = new Module{
    bind[UserDAO] to mock[UserDAO]
    bind[UserService] to new DefaultUserService
  }

  val duration = Duration.Inf
  def newService = inject[UserService]

  /**
   * generates  strings to be used in test
   * @param size the size of the string
   * @return the generated string
   */
  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  "User service" should {

    "implement createSession" in {
      val service = newService
      val us = UserSession(userId = guid, id = guid)
      service.repo.insertSession(any[UserSession]) returns Future.successful(Right(us))
      val s = Await.result(service.createSession(us.userId), duration)
      there was one(service.repo).insertSession(any[UserSession])
      s.errCode === 0
      s.value.get === us.id
    }

  }
}


