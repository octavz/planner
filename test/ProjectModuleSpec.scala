import org.planner.modules.core.ProjectModule
import org.planner.modules.dto._
import play.api.http.Status
import scaldi.{Module, Injectable}
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
class ProjectModuleSpec extends Specification with Mockito with Injectable {
  implicit val modules = new Module {
    bind[ProjectDAL] toProvider mock[ProjectDAL]
    bind[UserDAL] toProvider mock[UserDAL]
    bind[ProjectModule] toProvider new DefaultProjectModule
  }

  val authInfo = AuthInfo(user =
    User(id = guid, login = guid, password = guid, created = now, updated = now,
      lastLogin = nowo, providerToken = guido, nick = guid, userId = None, groupId = None), "1", None, None)

  def module = {
    val ret = inject[ProjectModule]
    ret.authData = authInfo
    ret
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
      val dto = ProjectDTO(id = guido, name = guid, desc = guido, parent = None, public = true, perm = None)

      m.dal.insertProject(any[Project], any[Group]) answers (a => dal(a.asInstanceOf[Project]))
      val s = Await.result(m.insertProject(dto), Duration.Inf)
      there was one(m.dal).insertProject(any[Project], any[Group])
      s must beRight
    }

    "implement updateProject and call dal" in {
      val m = module
      val dto = ProjectDTO(id = guido, name = guid, desc = guido, parent = None, public = true, perm = None)

      m.dal.updateProject(any[Project]) answers (a => dal(a.asInstanceOf[Project]))
      m.dal.getProjectById(any) returns dal(Some(genProject(authInfo.user.id)))
      val s = Await.result(m.updateProject(dto), Duration.Inf)
      there was one(m.dal).updateProject(any[Project])
      there was one(m.dal).getProjectById(dto.id.get)
      s must beRight
    }

    "return right error when dal crashes" in {
      val m = module
      val dto = ProjectDTO(id = guido, name = guid, desc = guido, parent = None, public = true, perm = Some(1))

      m.dal.insertProject(any[Project], any[Group]) returns Future.failed(new Exception("test"))
      val s = Await.result(m.insertProject(dto), Duration.Inf)
      there was one(m.dal).insertProject(any[Project], any[Group])
      s must beLeft
      val (code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "test"
    }

    "implement get user projects" in {
      val m = module
      m.dal.getUserProjects(anyString, any, any) returns dal(List())
      val s = m.getUserProjects(m.userId, 0, 100)
      s must not be (null)
    }

    "get user projects and call dal" in {
      val m = module
      val p1 = Project(id = guid, userId = "1", name = guid, description = guido, parentId = guido, created = now, updated = now)
      val g1 = Group(id = guid, projectId = p1.id, name = guid, updated = now, created = now, groupId = None, userId = m.authData.user.id)
      m.dal.getUserProjects(anyString, any, any) returns dal(List((g1, p1)))

      val s = Await.result(m.getUserProjects(m.userId, 0, 100), Duration.Inf)

      there was one(m.dal).getUserProjects(m.authData.user.id, 0, 100)
      s must beRight
      val ret = s.merge.asInstanceOf[ProjectListDTO]
      ret.items.size === 1
      ret.items(0).id.get === p1.id
      ret.items(0).name === p1.name
      ret.items(0).desc === p1.description
    }

    "get user project should handle dal errors" in {
      val m = module
      m.dal.getUserProjects(anyString, any, any) returns dalErr("Test error")
      val s = Await.result(m.getUserProjects(m.authData.user.id, 0, 100), Duration.Inf)
      there was one(m.dal).getUserProjects(m.authData.user.id, 0, 100)
      s must beLeft
      val (code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "Test error"
    }

    "get user projects and handle future failure" in {
      val m = module
      m.dal.getUserProjects(anyString, any, any) returns Future.failed(new RuntimeException("test future"))
      val s = Await.result(m.getUserProjects(m.authData.user.id, 0, 100), Duration.Inf)
      there was one(m.dal).getUserProjects(m.authData.user.id, 0, 100)
      s must beLeft
      val (code, message) = s.merge.asInstanceOf[ResultError]
      code === Status.INTERNAL_SERVER_ERROR
      message === "test future"
    }

    "get user public projects and call dal" in {
      val m = module
      val p1 = Project(id = guid, userId = "1", name = guid, description = guido, parentId = guido, created = now, updated = now)
      val g1 = Group(id = guid, projectId = p1.id, name = guid, updated = now, created = now, groupId = None, userId = m.authData.user.id)
      m.dal.getUserPublicProjects(anyString, any, any) returns dal(List((g1, p1)))
      m.dal.getUserProjects(anyString, any, any) returns dal(List((g1, p1)))
      val s = Await.result(m.getUserProjects("id", 0, 100), Duration.Inf)
      there was one(m.dal).getUserPublicProjects("id", 0, 100)
      s must beRight
    }

  }
}


