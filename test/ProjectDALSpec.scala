import org.junit.runner._
import org.planner.dal.impl.{SlickProjectDAL, TestCaching}
import org.planner.db.{Group, Project, User}
import org.planner.util.Gen._
import org.specs2.runner._
import play.api.test.WithApplication

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

/**
 * .
 * main test class for DefaultAssetServiceComponent
 * it mocks AssetRepoComponent
 */
@RunWith(classOf[JUnitRunner])
class ProjectDALSpec extends BaseDALSpec {

  import config.driver.api._

  def insertRandomUser(): User = {
    val u = randUser
    waitFor(db.run(Users += u))
    u
  }

  def insertRandomProject(uid: String): (Project, Group) = {
    val p = randProject(uid)
    val g = randGroup(p)
    val action = (for {
      _ <- Projects += p
      _ <- Groups += g
    } yield ()).transactionally
    dbSync(action)
    (p, g)
  }

  def newDal = new SlickProjectDAL(new TestCaching)

  "Project DAL" should {

    "insert project and the default group" in new WithApplication(testApp) {

      import org.planner.util.Time._

      val dal = newDal
      val p = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None, created = now, updated = now)
      val g = Group(id = guid, projectId = p.id, name = p.name, created = now, updated = now, userId = testUser.id, groupId = None)
      val res = waitFor(dal.insertProject(p, g))
      res must beAnInstanceOf[Project]
      val lstProjects = dbSync(Projects.filter(_.id === p.id).result)
      lstProjects.size === 1
      val lstGroups = dbSync(Groups.filter(_.id === g.id).result).toList
      lstGroups.size === 1
      val lstGroupsUsers = dbSync(GroupsUsers.filter(_.groupId === g.id).result).toList
      lstGroupsUsers.size === 1
    }

    "get private projects by user" in new WithApplication(testApp) {
      val dal = newDal
      val resGet = Await.result(dal.getUserProjects(testUser.id, 0, 100), Duration.Inf)
      val ret = resGet.asInstanceOf[(List[(Group, Project)], Int)]
      ret._1.size === 1
      ret._1(0)._2 === testProject
    }

    "get public projects by user" in new WithApplication(testApp) {
      val dal = newDal
      val resGet = Await.result(dal.getUserPublicProjects("1", 0, 1), Duration.Inf)
      val ret = resGet.asInstanceOf[(List[(Group, Project)], Int)]
      ret._1.size === 0
    }

    "get project groups" in new WithApplication(testApp) {
      val dal = newDal
      val projects = Await.result(dal.getProjectGroups(testProject.id), Duration.Inf)
      projects must beAnInstanceOf[List[Group]]
      projects.size === 1
    }
  }
}

