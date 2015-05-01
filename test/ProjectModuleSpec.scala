import org.planner.modules.dto._
import play.api.http.Status
import org.planner.util.Gen._
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import scala.concurrent._
import scala.concurrent.duration._
import org.specs2.mock._

import org.planner.util.Time._
import org.planner.modules._
import org.planner.modules.core.impl._
import org.planner.dal._
import org.planner.db._
import org.planner.util.Time._

import scalaoauth2.provider.AuthInfo

/** .
  * main test class for DefaultAssetServiceComponent
  * it mocks AssetRepoComponent
  */
@RunWith(classOf[JUnitRunner])
class ProjectModuleSpec extends Specification with Mockito {

  val authInfo = AuthInfo(user =
    User(id = guid, login = guid, password = guid, created = now, updated = now,
      lastLogin = nowo, providerToken = guido, nick = guid, userId = None, groupId = None), Some("1"), None, None)

  def module = {
    new DefaultProjectModuleComponent with ProjectDALComponent with UserDALComponent {
      override val projectDal: ProjectDAL = mock[ProjectDAL]
      override val dalUser: UserDAL = mock[UserDAL]

      _authData = authInfo
    }
  }

  /**
   * generates  strings to be used in test
   * @param size the size of the string
   * @return the generated string
   */
  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  def genProject(userId: String) = Project(id = guid, userId = userId, name = guid, description = guido, parentId = None, created = now, updated = now)

  "Project module" should {

    "implement insertProject and call dal" in {
      val m = module
      m.authData === authInfo
      val dto = ProjectDTO(id = guido, name = guid, desc = guido, parent = None, public = true, perm = None, groupId = Some("groupId"))

      m.projectDal.insertProject(any[Project], any[Group]) answers (a => dal(a.asInstanceOf[Project]))
      val s = Await.result(m.projectModule.insertProject(dto), Duration.Inf)
      there was one(m.projectDal).insertProject(any[Project], any[Group])
      s must beRight
    }

    "implement updateProject and call dal" in {
      val m = module
      val dto = ProjectDTO(id = guido, name = guid, desc = guido, parent = None, public = true, perm = None, groupId = Some("groupId"))

      m.projectDal.updateProject(any[Project]) answers (a => dal(a.asInstanceOf[Project]))
      m.projectDal.getProjectById(any) returns dal(Some(genProject(authInfo.user.id)))
      val s = Await.result(m.projectModule.updateProject(dto), Duration.Inf)
      there was one(m.projectDal).updateProject(any[Project])
      there was one(m.projectDal).getProjectById(dto.id.get)
      s must beRight
    }

    "return right error when dal crashes" in {
      val m = module
      val dto = ProjectDTO(id = guido, name = guid, desc = guido, parent = None, public = true, perm = Some(1), groupId = Some("groupId"))

      m.projectDal.insertProject(any[Project], any[Group]) returns Future.failed(new Exception("test"))
      val s = Await.result(m.projectModule.insertProject(dto), Duration.Inf)
      there was one(m.projectDal).insertProject(any[Project], any[Group])
      s must beLeft
      val (code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "test"
    }

    "implement get user projects" in {
      val m = module
      m.projectDal.getUserProjects(anyString, any, any) returns dal(List())
      val s = m.projectModule.getUserProjects(m.userId, 0, 100)
      s must not be (null)
    }

    "get user projects and call dal" in {
      val m = module
      val p1 = Project(id = guid, userId = "1", name = guid, description = guido, parentId = guido, created = now, updated = now)
      val g1 = Group(id = guid, projectId = p1.id, name = guid, updated = now, created = now, groupId = None, userId = m.authData.user.id)
      m.projectDal.getUserProjects(anyString, any, any) returns dal(List((g1, p1)))

      val s = Await.result(m.projectModule.getUserProjects(m.userId, 0, 100), Duration.Inf)

      there was one(m.projectDal).getUserProjects(m.authData.user.id, 0, 100)
      s must beRight
      val ret = s.merge.asInstanceOf[ProjectListDTO]
      ret.items.size === 1
      ret.items(0).id.get === p1.id
      ret.items(0).name === p1.name
      ret.items(0).desc === p1.description
    }

    "get user project should handle dal errors" in {
      val m = module
      m.projectDal.getUserProjects(anyString, any, any) returns dalErr("Test error")
      val s = Await.result(m.projectModule.getUserProjects(m.authData.user.id, 0, 100), Duration.Inf)
      there was one(m.projectDal).getUserProjects(m.authData.user.id, 0, 100)
      s must beLeft
      val (code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "Test error"
    }

    "get user projects and handle future failure" in {
      val m = module
      m.projectDal.getUserProjects(anyString, any, any) returns Future.failed(new RuntimeException("test future"))
      val s = Await.result(m.projectModule.getUserProjects(m.authData.user.id, 0, 100), Duration.Inf)
      there was one(m.projectDal).getUserProjects(m.authData.user.id, 0, 100)
      s must beLeft
      val (code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "test future"
    }

    "get user public projects and call dal" in {
      val m = module
      val p1 = Project(id = guid, userId = "1", name = guid, description = guido, parentId = guido, created = now, updated = now)
      val g1 = Group(id = guid, projectId = p1.id, name = guid, updated = now, created = now, groupId = None, userId = m.authData.user.id)
      m.projectDal.getUserPublicProjects(anyString, any, any) returns dal(List((g1, p1)))
      m.projectDal.getUserProjects(anyString, any, any) returns dal(List((g1, p1)))
      val s = Await.result(m.projectModule.getUserProjects("id", 0, 100), Duration.Inf)
      there was one(m.projectDal).getUserPublicProjects("id", 0, 100)
      s must beRight
    }

  }
}


