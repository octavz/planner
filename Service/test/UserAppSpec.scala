import controllers._

import org.planner.controllers.{UserController, MainController}
import org.planner.dal.Oauth2DAL
import org.planner.modules.core.UserModule
import org.planner.modules.dto._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json._
import org.planner.util.Gen._

import play.api.GlobalSettings
import play.api.test._
import play.api.test.Helpers._
import scaldi.Module
import scaldi.play.ScaldiSupport
import org.planner.modules._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class UserAppSpec extends Specification with Mockito {

  def app(userService: UserModule = mock[UserModule]) = FakeApplication(
    additionalConfiguration = Map(
      "evolutionplugin" -> "disabled",
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"),
    withoutPlugins = Seq("com.typesafe.plugin.RedisPlugin"),
    withGlobal = Some(
      new GlobalSettings with ScaldiSupport {
        def applicationModule = new Module {
          binding toProvider new MainController
        } :: new Module {
          bind[UserModule] toProvider userService
          bind[Oauth2DAL] toProvider mock[Oauth2DAL]
          binding toProvider new UserController
        }
      }))

  "Application" should {

    "have external static files attached to root" in new WithApplication(app()) {
      route(FakeRequest(GET, "/boum")) must beSome
    }

    "render login page" in new WithApplication(app()) {
      val page = route(FakeRequest(GET, "/login")).get
      status(page) must equalTo(OK)
      contentType(page) must beSome.which(_ == "text/html")
      contentAsString(page) must contain("login")

    }

    "have login route" in new WithApplication(app()) {
      val page = route(FakeRequest(POST, "/login")
        .withFormUrlEncodedBody("email" -> "test@test.com", "password" -> "12345"))
        .get
      status(page) must equalTo(OK)
    }

    "have register route" in {
      val service = mock[UserModule]
      service.registerUser(any[UserDTO]) answers (u => result(u.asInstanceOf[UserDTO]))
      running(app(service)) {
        val page = route(FakeRequest(POST, "/register")
          .withJsonBody(Json.parse(
          """
            {
            "login":"test@test.com",
            "password":"123456"
            }
          """)))
        page must beSome
        val res = Await.result(page.get, Duration.Inf)
        val json = contentAsJson(page.get)
        json \ "login" === JsString("test@test.com")
      }
    }

    "have add group route" in {
      val service = mock[UserModule]
      service.addGroup(any[GroupDTO]) answers {
        u =>
          val dto = u.asInstanceOf[GroupDTO].copy(id = guido)
          result(dto)
      }
      running(app(service)) {
        val page = route(FakeRequest(POST, "/group")
          .withJsonBody(Json.parse(
          """
            {
            "name":"group name",
            "projectId" : "pid"
            }
          """)))
        page must beSome
        val res = Await.result(page.get, Duration.Inf)
        val json = contentAsJson(page.get)
        json \ "name" === JsString("group name")
        json \ "projectId" === JsString("pid")
        (json \ "id").as[JsString].value must contain("-")
      }

    }

  }
}
