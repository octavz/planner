import org.planner.util.Gen._


import org.junit.runner._
import org.planner.dal._
import org.planner.dal.impl.{SlickProjectDALComponent, TestCaching}
import org.planner.db.{User, Group, Project}
import org.specs2.runner._
import play.api.test.WithApplication
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


  def insertRandomUser()(implicit s: Session): User = {
    val u = randUser
    Users.insert(u)
    u
  }

  def insertRandomProject(uid: String)(implicit s: Session): (Project, Group) = {
    val p = randProject(uid)
    val g = randGroup(p)
    Projects.insert(p)
    Groups.insert(g)
    (p,g)
  }

  def newDal = new SlickProjectDALComponent with TestCaching{}.dalProject

  "Project DAL" should {

    "insert project and the default group" in new WithApplication(testApp) {

      import org.planner.util.Time._

      DB.withSession {
        implicit s =>
          val dal = newDal
          val p = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None, created = now, updated = now)
          val g = Group(id = guid, projectId = p.id, name = p.name, created = now, updated = now, userId = testUser.id, groupId = None)
          val res = Await.result(dal.insertProject(p, g), Duration.Inf)
          res must beAnInstanceOf[Project]
          val lstProjects = Projects.filter(_.id === p.id).list
          lstProjects.size === 1 
          val lstGroups = Groups.filter(_.id === g.id).list
          lstGroups.size === 1
          val lstGroupsUsers = GroupsUsers.filter(_.groupId === g.id).list
          lstGroupsUsers.size === 1
      }
    }

    "get private projects by user" in new WithApplication(testApp) {
      DB.withSession {
        implicit s =>
          val dal = newDal
          val resGet = Await.result(dal.getUserProjects(testUser.id, 0, 100), Duration.Inf)
          val ret = resGet.asInstanceOf[(List[(Group,Project)], Int)]
          ret._1.size === 1
          ret._1(0)._2 === testProject
      }
    }

    "get public projects by user" in new WithApplication {
      DB.withSession {
        implicit s =>
          val dal = newDal
          val resGet = Await.result(dal.getUserPublicProjects("1", 0, 1), Duration.Inf)
          val ret = resGet.asInstanceOf[(List[(Group,Project)], Int)]
          ret._1.size === 0
      }
    }

    "get project groups" in new WithApplication(testApp) {
      val dal = newDal
      val projects = Await.result(dal.getProjectGroups(testProject.id), Duration.Inf)
      projects must beAnInstanceOf[List[Group]]
      projects.size === 1
    }
  }
}

