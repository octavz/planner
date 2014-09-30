import org.planner.dal._
import org.planner.dal.impl.{ TestCaching, SlickOauth2DAL, SlickUserDAL }
import org.planner.db._
import org.junit.runner._
import org.specs2.execute.AsResult
import org.specs2.mock._
import org.specs2.mutable._
import org.specs2.runner._
import org.specs2.specification.AroundExample
import play.api.test.{ FakeApplication, WithApplication }
import scaldi.{ Module, Injectable }
import org.planner.util.Gen._
import org.planner.util.Time._
import scala.concurrent._
import scala.concurrent.duration._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.test.Helpers._
import scala.util.Try
import play.api.test.WithApplication

/**
 * .
 * main test class for DefaultAssetServiceComponent
 * it mocks AssetRepoComponent
 */
@RunWith(classOf[JUnitRunner])
class UserDALSpec extends BaseDALSpec {
  implicit val modules = new Module {
    bind[Caching] to new TestCaching
  }

  lazy val dao = new SlickUserDAL

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
      val p = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None, created =now, updated= now)
      val u = newUser
      DB.withSession {
        implicit session =>
          Projects.insert(p) === 1
          Users.insert(u) === 1
          val model = Group(id = guid, projectId = p.id, name = guid, updated = now, created = now, userId = u.id, groupId = None)
          val res = Await.result(dao.insertGroupWithUser(model, testUser.id), Duration.Inf)
          Groups.filter(_.id === model.id).list.size === 1
          //val res1 = Await.result(dao.insertGroupsUser(GroupsUser(groupId = model.id, userId = u.id)), Duration.Inf)
          GroupsUsers.filter(_.groupId === model.id).list.size === 1
      }

      "insertGroup, insertGroupsUser" in new WithApplication(testApp) {
        val p = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None, created = now, updated = now)
        val u = newUser
        DB.withSession {
          implicit session =>
            Projects.insert(p) === 1
            Users.insert(u) === 1
            val model = Group(id = guid, projectId = p.id, name = guid, updated = now, created = now, userId = u.id, groupId = None)
            val res = Await.result(dao.insertGroup(model), Duration.Inf)
            Groups.filter(_.id === model.id).list.size === 1
            val res1 = Await.result(dao.insertGroupsUser(GroupsUser(groupId = model.id, userId = u.id)), Duration.Inf)
            GroupsUsers.filter(_.groupId === model.id).list.size === 1
        }
      }
    }

    "get user user group ids" in new WithApplication(testApp) {
      DB.withSession {
        implicit session =>
          val p = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None, created = now, updated = now)
          Projects.insert(p) === 1
          val model = Group(id = guid, projectId = p.id, name = guid, updated = now, created = now, userId = testUser.id, groupId = None)
          val model1 = Group(id = guid, projectId = p.id, name = guid, updated = now, created = now, userId = testUser.id, groupId = None)
          Groups.insertAll(model, model1) === Some(2)
          GroupsUsers.insertAll(GroupsUser(groupId = model.id, userId = testUser.id),
            GroupsUser(groupId = model1.id, userId = testUser.id )) === Some(2)
      }
      val groups = Await.result(dao.getUserGroups(testUser.id), Duration.Inf)
      groups.size === 3
    }
  }
}


