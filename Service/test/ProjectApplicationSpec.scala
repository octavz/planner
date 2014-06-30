import org.planner.controllers.ProjectController
import org.planner.util.Gen._
import org.planner.util.Time._
import controllers._
import org.planner.dao.Oauth2DAO
import org.planner.db.User
import org.planner.services.dto._
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
import org.planner.services._

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

  def anUser = User(guid, guido, 0, guido, nowo, nowo, nowo, 0, guido)

  def app(service: ProjectService = mock[ProjectService], u: User = anUser) = FakeApplication(
    withoutPlugins = Seq("com.typesafe.plugin.RedisPlugin"),
    withGlobal = Some(
      new GlobalSettings with ScaldiSupport {
        def applicationModule = {
          val auth = mock[Oauth2DAO]
          auth.findAccessToken(anyString) returns Some(AccessToken("token", None, None, None, new java.util.Date()))
          auth.isAccessTokenExpired(any[AccessToken]) returns false
          auth.findAuthInfoByAccessToken(any[AccessToken]) returns Some(authInfo)
          new Module {
            bind[Oauth2DAO] toProvider auth
            bind[ProjectService] toProvider service
            binding toProvider new ProjectController
          }
        }
      }))

  implicit val authInfo = new AuthData(anUser, "1", None, None)

  "Application" should {

    "have create project route and authorize" in {
      val service = mock[ProjectService]
      service.insertProject(any[ProjectDTO]) answers (p => retService(p.asInstanceOf[ProjectDTO]))
      running(app(service)) {
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
        there was one (service).authInfo_=(any[AuthData])
        there was one(service).insertProject(any[ProjectDTO])
        val json = contentAsJson(page.get)
        json \ "name" === JsString("project")
        json \ "desc" === JsString("123456")
        json \ "parent" === JsString("parent")
      }
    }


  }
}
