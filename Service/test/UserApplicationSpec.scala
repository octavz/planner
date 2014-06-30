import controllers._
import org.planner.controllers.{UserController, MainController}
import org.planner.dao.Oauth2DAO
import org.planner.services.dto._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json._

import play.api.GlobalSettings
import play.api.test._
import play.api.test.Helpers._
import scaldi.Module
import scaldi.play.ScaldiSupport
import org.planner.services._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class UserApplicationSpec extends Specification with Mockito {

  def app(userService: UserService = mock[UserService]) = FakeApplication(
    withoutPlugins = Seq("com.typesafe.plugin.RedisPlugin"),
    withGlobal = Some(
      new GlobalSettings with ScaldiSupport {
        def applicationModule = new Module {
          binding toProvider new MainController
        } :: new Module {
          bind[UserService] toProvider userService
          bind[Oauth2DAO] toProvider mock[Oauth2DAO]
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
      val service = mock[UserService]
      service.registerUser(any[UserDTO]) answers (u => retService(u.asInstanceOf[UserDTO]))
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

  }
}
