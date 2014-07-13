import org.planner.util.Gen._

import org.junit.runner._
import org.planner.dal._
import org.planner.dal.impl.{SlickProjectDAL, TestCaching}
import org.planner.db.Project
import org.specs2.runner._
import play.api.test.WithApplication
import scaldi.Module
import scala.concurrent._
import scala.concurrent.duration._
import play.api.test.Helpers._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB

/** .
  * main test class for DefaultAssetServiceComponent
  * it mocks AssetRepoComponent
  */
@RunWith(classOf[JUnitRunner])
class ProjectDALSpec extends BaseDALSpec {
  implicit val modules = new Module {
    bind[Caching] to new TestCaching
  }

  "Project DAL" should {

    "insert project" in new WithApplication(testApp) {
      DB.withSession {
        implicit s =>
          val dal = new SlickProjectDAL()
          val p = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None)
          val res = Await.result(dal.insertProject(p), Duration.Inf)
          res must beRight
          res.merge.asInstanceOf[Project] === p
          val lst = Projects.where(_.id === p.id).list
          lst.size === 1
      }
    }

    "get projects by user" in new WithApplication(testApp) {
      DB.withSession {
        implicit s =>
          val dal = new SlickProjectDAL()
          val p = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None)
          val res = Await.result(dal.insertProject(p), Duration.Inf)
          res must beRight
          val resGet = Await.result(dal.getUserProjects(testUser.id), Duration.Inf)
          resGet must beRight
          val ret = resGet.merge.asInstanceOf[List[Project]]
          ret.size === 1
          ret(0) === p
      }
    }

  }
}

