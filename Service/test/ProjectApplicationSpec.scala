import org.planner.controllers.ProjectController
import org.planner.modules.core.ProjectModule
import org.planner.util.Gen._
import org.planner.util.Time._
import controllers._
import org.planner.dal.Oauth2DAL
import org.planner.db.User
import org.planner.modules.dto._
import org.junit.runner._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import play.api.GlobalSettings
import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._
import scaldi.Module
import scaldi.play.ScaldiSupport
import org.planner.modules._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scalaoauth2.provider.{AccessToken, AuthInfo}

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ProjectApplicationSpec extends Specification with Mockito {

  def anUser = User(guid, guid, 0, guido, now, now, nowo, 0, guid)

  def app(module: ProjectModule = mock[ProjectModule], u: User = anUser) = FakeApplication(
    withoutPlugins = Seq("com.typesafe.plugin.RedisPlugin"),
    withGlobal = Some(
      new GlobalSettings with ScaldiSupport {
        def applicationModule = {
          val auth = mock[Oauth2DAL]
          auth.findAccessToken(anyString) returns Some(AccessToken("token", None, None, None, new java.util.Date()))
          auth.isAccessTokenExpired(any[AccessToken]) returns false
          auth.findAuthInfoByAccessToken(any[AccessToken]) returns Some(authInfo)
          new Module {
            bind[Oauth2DAL] toProvider auth
            bind[ProjectModule] toProvider module
            binding toProvider new ProjectController
          }
        }
      }))

  implicit val authInfo = new AuthData(anUser, "1", None, None)

  "Application" should {

    "have create project route and authorize" in {
      val module = mock[ProjectModule]
      module.insertProject(any[ProjectDTO]) answers (p => result(p.asInstanceOf[ProjectDTO]))
      running(app(module)) {
        val page = route(FakeRequest(POST, "/project")
          .withHeaders("Authorization" -> "OAuth token")
          .withJsonBody(Json.parse(
          """
            {
            "name":"project",
            "desc":"123456",
            "parent":"parent"
            }
          """)))
        page must beSome
        val res = Await.result(page.get, Duration.Inf)
        there was one (module).authData_=(any[AuthData])
        there was one(module).insertProject(any[ProjectDTO])
        val json = contentAsJson(page.get)
        json \ "name" === JsString("project")
        json \ "desc" === JsString("123456")
        json \ "parent" === JsString("parent")
      }
    }


  }
}
