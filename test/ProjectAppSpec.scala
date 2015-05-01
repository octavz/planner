import org.planner.controllers.ProjectController
import org.planner.dal.Oauth2DALComponent
import org.planner.modules.core.ProjectModuleComponent
import org.planner.util.Gen._
import org.planner.util.Time._
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
import org.planner.modules._

import scala.concurrent._
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

  def app(m: ProjectController = mock[ProjectController], u: User = anUser) = FakeApplication(
    additionalConfiguration = Map(
      "evolutionplugin" -> "disabled",
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"),
    withoutPlugins = Seq("com.typesafe.plugin.RedisPlugin"),
    withGlobal = Some(
      new GlobalSettings {
        override def getControllerInstance[A](clazz: Class[A]) = clazz match {
          case c if c.isAssignableFrom(classOf[ProjectController]) => m.asInstanceOf[A]
          case _ => super.getControllerInstance(clazz)
        }
      }
    ))

  implicit val authInfo = new AuthData(anUser, Some("1"), None, None)

  def newComp = new ProjectController with ProjectModuleComponent {
    override val dalAuth: Oauth2DAL = mock[Oauth2DAL]
    override val projectModule = mock[ProjectModule]
    dalAuth.findAccessToken(anyString) returns Future.successful(Some(AccessToken("token", None, None, None, new java.util.Date())))
    //auth.isAccessTokenExpired(any[AccessToken]) returns false
    dalAuth.findAuthInfoByAccessToken(any[AccessToken]) returns Future.successful(Some(authInfo))
  }

  "Project controller" should {

    "have create project route and authorize" in {
      val module = newComp
      module.projectModule.insertProject(any[ProjectDTO]) answers (p => result(p.asInstanceOf[ProjectDTO]))
      running(app(module)) {
        val page = route(FakeRequest(POST, "/api/project")
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
        module.authData === authInfo
        there was one(module.projectModule).insertProject(any[ProjectDTO])
        val json = contentAsJson(page.get)
        json \ "name" === JsString("project")
        json \ "desc" === JsString("123456")
        json \ "parent" === JsString("parent")
      }
    }

    "get all projects" in {
      val module = newComp
      val p = ProjectDTO(id = guido, name = guid, desc = guido, parent = guido, public = true, perm = Some(1), groupId = Some("groupId"))
      module.projectModule.getUserProjects("id", 0, 10) returns result(ProjectListDTO(items = List(p)))
      running(app(module)) {
        val page = route(FakeRequest(GET, "/api/user/id/projects?offset=0&count=10").withHeaders("Authorization" -> "OAuth token"))
        page must beSome
        status(page.get) === OK
        Await.ready(page.get, Duration.Inf)
        module.authData === authInfo
        there was one(module.projectModule).getUserProjects("id", 0, 10)
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

    "update project" in {
      val module = newComp
      val p = ProjectDTO(id = guido, name = guid, desc = guido, parent = guido, public = true, perm = Some(1), groupId = Some("groupId"))
      module.projectModule.updateProject(any) returns result(p)
      running(app(module)) {
        val page = route(FakeRequest(PUT, "/api/project/id").withHeaders("Authorization" -> "OAuth token").withJsonBody(Json.parse(
          s"""
            {
            "name":"${p.name}",
            "desc":"${p.desc}",
            "parent":"${p.parent}",
            "public" : true
            }
          """)))
        Await.ready(page.get, Duration.Inf)
        val json = contentAsJson(page.get)
        page must beSome
        status(page.get) === OK
        module.authData === authInfo
        there was one(module.projectModule).updateProject(any)
        json \ "name" === JsString(p.name)
        json \ "desc" === JsString(p.desc.get)
        json \ "parent" === JsString(p.parent.get)

      }

    }

  }
}
