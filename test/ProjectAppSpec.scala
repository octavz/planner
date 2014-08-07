import org.planner.controllers.ProjectController
import org.planner.modules.core.ProjectModule
import org.planner.util.Gen._
import org.planner.util.Time._
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
class ProjectAppSpec extends Specification with Mockito {

  def anUser = User(id = guid, login = guid, providerToken = None, created = now, userId = None, groupId = None, updated = now, lastLogin = None, password = guid, nick = guid)

  def app(module: ProjectModule = mock[ProjectModule], u: User = anUser) = FakeApplication(
    additionalConfiguration = Map(
      "evolutionplugin" -> "disabled",
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"),
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

  "Project controller" should {

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
            "parent":"parent",
            "public" : true 
            }
          """)))
        page must beSome
        Await.result(page.get, Duration.Inf)
        there was one(module).authData_=(any[AuthData])
        there was one(module).insertProject(any[ProjectDTO])
        val json = contentAsJson(page.get)
        json \ "name" === JsString("project")
        json \ "desc" === JsString("123456")
        json \ "parent" === JsString("parent")
      }
    }

    "get all projects" in {
      val module = mock[ProjectModule]
      val p = ProjectDTO(id = guido, name = guid, desc = guido, parent = guido, public = true, perm = Some(1))
      module.getUserProjects("id", 0, 10) returns result(ProjectListDTO(items = List(p)))
      running(app(module)) {
        val page = route(FakeRequest(GET, "/user/id/projects?offset=0&count=10").withHeaders("Authorization" -> "OAuth token"))
        page must beSome
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        there was one(module).authData_=(any[AuthData])
        there was one(module).getUserProjects("id",0, 10)
        val json = contentAsJson(page.get)
        val arr = (json \ "items").as[JsArray].value
        arr.size === 1
        arr(0) \ "id" === JsString(p.id.get)
        arr(0) \ "name" === JsString(p.name)
        arr(0) \ "desc" === JsString(p.desc.get)
        arr(0) \ "parent" === JsString(p.parent.get)
        arr(0) \ "public" === JsBoolean(true)
        arr(0) \ "perm" === JsNumber(p.perm.get)
      }

    }

  }
}
