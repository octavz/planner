import org.planner.dal._
import org.planner.dal.impl.{SlickOauth2DAL, SlickUserDAL}
import org.planner.db._
import org.junit.runner._
import org.specs2.execute.AsResult
import org.specs2.mock._
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.specification.AroundExample
import play.api.test.{FakeApplication, WithApplication}
import scaldi.{Injectable}
import org.planner.util.Gen._
import org.planner.util.Time._
import scala.concurrent._
import scala.concurrent.duration._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.test.Helpers._
import scala.util.Try

/** .
  * main test class for DefaultAssetServiceComponent
  * it mocks AssetRepoComponent
  */
@RunWith(classOf[JUnitRunner])
class UserDALSpec extends BaseDaoSpec {

  lazy val dao = new SlickUserDAL

  "User org.planner.dao" should {

    "insertSession, findSessionById, deleteSessionByUser" in new WithApplication {
      val us = UserSession(guid, guid)
      val res = Await.result(dao.insertSession(us), Duration.Inf)
      res must beRight
      val ret = Await.result(dao.findSessionById(us.id), Duration.Inf)
      ret must beRight
      val v = ret.merge.asInstanceOf[Option[UserSession]]
      v must beSome
      v === Some(us)
      val retDelete = Await.result(dao.deleteSessionByUser(us.userId), Duration.Inf)
      retDelete must beRight
      retDelete.merge.asInstanceOf[Int] === 1
    }

    "insertUser,getUserById, getUserByEmail" in new WithApplication {
      val usr = User(guid, guid, 0, guido, now, now, nowo, 0, guid)
      val res = Await.result(dao.insertUser(usr), Duration.Inf)
      res must beRight
      val ret = Await.result(dao.getUserById(usr.id), Duration.Inf)
      ret must beRight
      val v = ret.merge.asInstanceOf[User]
      v === usr
      val ret1 = Await.result(dao.getUserByEmail(usr.login), Duration.Inf)
      ret1 must beRight
      val v1 = ret.merge.asInstanceOf[User]
      v1 === usr
    }

  }
}


