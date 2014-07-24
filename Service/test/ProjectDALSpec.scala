import org.planner.util.Gen._

import org.junit.runner._
import org.planner.dal._
import org.planner.dal.impl.{SlickProjectDAL, TestCaching}
import org.planner.db.{Group, Project}
import org.specs2.runner._
import play.api.test.WithApplication
import scaldi.Module
import scala.concurrent._
import scala.concurrent.duration._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import org.planner.util.Time._

/**
 * .
 * main test class for DefaultAssetServiceComponent
 * it mocks AssetRepoComponent
 */
@RunWith(classOf[JUnitRunner])
class ProjectDALSpec extends BaseDALSpec {
  implicit val modules = new Module {
    bind[Caching] to new TestCaching
  }

  "Project DAL" should {

    "insert project and the default group" in new WithApplication(testApp) {

      import org.planner.util.Time._

      DB.withSession {
        implicit s =>
          val dal = new SlickProjectDAL()
          val p = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None, created = now, updated = now)
          val g = Group(id = guid, projectId = p.id, name = p.name, created = now, updated = now, userId = testUser.id, groupId = None)
          val res = Await.result(dal.insertProject(p, g), Duration.Inf)
          res must beAnInstanceOf[Project]
          val lstProjects = Projects.where(_.id === p.id).list
          lstProjects.size === 1 
          val lstGroups = Groups.where(_.id === g.id).list
          lstGroups.size === 1
          val lstGroupsUsers = GroupsUsers.where(_.groupId === g.id).list
          lstGroupsUsers.size === 1
      }
    }

    "get projects by user" in new WithApplication(testApp) {
      DB.withSession {
        implicit s =>
          val dal = new SlickProjectDAL()
          val resGet = Await.result(dal.getUserProjects(testUser.id), Duration.Inf)
          val ret = resGet.asInstanceOf[List[(Group,Project)]]
          ret.size === 1
          ret(0)._2 === testProject 
      }
    }

    "get project groups" in new WithApplication(testApp) {
      val dal = new SlickProjectDAL()   
      val projects = Await.result(dal.getProjectGroupIds(testProject.id), Duration.Inf)
      projects must beAnInstanceOf[List[String]]
      projects.size === 1
    }

  }
}

