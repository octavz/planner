import org.planner.modules.core.ProjectModule
import org.planner.modules.dto._
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
    bind[ProjectModule] toProvider new DefaultProjectModule
  }

  val duration = Duration.Inf

  def module = {
    val ret = inject[ProjectModule]
    ret.authData = AuthInfo[User](user =
      User(id = guid, login = guid, password = guid, created = now, updated = now,
        lastLogin = nowo, openidToken = guido, nick = guid), "1", None, None)
    ret
  }

  /**
   * generates  strings to be used in test
   * @param size the size of the string
   * @return the generated string
   */
  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString

  "Project service" should {

    "implement save project and call dal" in {
      val m = module
      val dto = ProjectDTO(name = guid, desc = guido, parent = None)
      m.dal.insertProject(any[Project]) answers (a => dao(a.asInstanceOf[Project]))
      val s = Await.result(m.insertProject(dto), duration)
      there was one(m.dal).insertProject(any[Project])
      s must beRight
    }

  }
}


