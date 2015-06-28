import org.junit.runner._
import org.planner.dal.impl.{SlickUserDAL, TestCaching}
import org.planner.db._
import org.planner.util.Gen._
import org.planner.util.Time._
import org.specs2.runner._
import play.api.test.WithApplication
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

/**
 * .
 * main test class for DefaultAssetServiceComponent
 * it mocks AssetRepoComponent
 */
@RunWith(classOf[JUnitRunner])
class UserDALSpec extends BaseDALSpec {

  import config.driver.api._

  lazy val dao = new SlickUserDAL(new TestCaching)

  def newUser = User(id = guid, login = guid, providerToken = None, created = now, userId = None, groupId = None,
    updated = now, lastLogin = None, password = guid, nick = guid)

  "User DAL" should {

    "insertSession, findSessionById, deleteSessionByUser" in new WithApplication(testApp) {
      val us = UserSession(id = guid, userId = testUser.id)
      val res = Await.result(dao.insertSession(us), Duration.Inf)
      val ret = Await.result(dao.findSessionById(us.id), Duration.Inf)
      val v = ret.asInstanceOf[Option[UserSession]]
      v must beSome
      v === Some(us)
      val retDelete = Await.result(dao.deleteSessionByUser(us.userId), Duration.Inf)
      retDelete.asInstanceOf[Int] === 1
    }

    "insertUser,getUserById, getUserByEmail" in new WithApplication(testApp) {
      val usr = newUser
      val res = Await.result(dao.insertUser(usr), Duration.Inf)
      val ret = Await.result(dao.getUserById(usr.id), Duration.Inf)
      val v = ret.asInstanceOf[User]
      v === usr
      val ret1 = Await.result(dao.getUserByEmail(usr.login), Duration.Inf)
      val v1 = ret.asInstanceOf[User]
      v1 === usr
    }

    "insertGroupWithUser" in new WithApplication(testApp) {
      val p = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None, created = now, updated = now)
      val u = newUser
      val a = (for {
        _ <- Projects += p
        _ <- Users += u
      } yield ()).transactionally
      val model = Group(id = guid, projectId = p.id, name = guid, updated = now, created = now, userId = u.id, groupId = None)
      val res = Await.result(dao.insertGroupWithUser(model, testUser.id), Duration.Inf)
      dbSync(Groups.filter(_.id === model.id).result).size === 1
      //val res1 = Await.result(dao.insertGroupsUser(GroupsUser(groupId = model.id, userId = u.id)), Duration.Inf)
      dbSync(GroupsUsers.filter(_.groupId === model.id).result).size === 1
    }

    "insertGroup, insertGroupsUser" in new WithApplication(testApp) {
      val p = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None, created = now, updated = now)
      val u = newUser
      val a = (for {
        _ <- Projects += p
        _ <- Users += u
      } yield ()).transactionally
      dbSync(a)
      val model = Group(id = guid, projectId = p.id, name = guid, updated = now, created = now, userId = u.id, groupId = None)
      val res = Await.result(dao.insertGroup(model), Duration.Inf)
      dbSync(Groups.filter(_.id === model.id).result).size === 1
      val res1 = Await.result(dao.insertGroupsUser(GroupsUser(groupId = model.id, userId = u.id)), Duration.Inf)
      dbSync(GroupsUsers.filter(_.groupId === model.id).result).size === 1
    }

    "get user user group ids" in new WithApplication(testApp) {
      val p = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None, created = now, updated = now)
      val model = Group(id = guid, projectId = p.id, name = guid, updated = now, created = now, userId = testUser.id, groupId = None)
      val model1 = Group(id = guid, projectId = p.id, name = guid, updated = now, created = now, userId = testUser.id, groupId = None)
      val a = (for {
        _ <- Projects += p
        _ <- GroupsUsers += GroupsUser(groupId = model.id, userId = testUser.id)
        _ <- GroupsUsers += GroupsUser(groupId = model1.id, userId = testUser.id)
        _ <- Groups += model
        _ <- Groups += model1
      } yield ()).transactionally
      dbSync(a)
      val groups = Await.result(dao.getUserGroupsIds(testUser.id), Duration.Inf)
      groups.size === 3
    }
  }
}


