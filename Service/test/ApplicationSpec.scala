import controllers._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.GlobalSettings
import play.api.test._
import play.api.test.Helpers._
import scaldi.Module
import scaldi.play.ScaldiSupport
import services.UserService

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification with Mockito {

  def app = FakeApplication(
    withoutPlugins = Seq("com.typesafe.plugin.RedisPlugin"),
    withGlobal = Some(
      new GlobalSettings with ScaldiSupport {
        def applicationModule = new Module {
          binding to new MainController
        } :: new Module {
          bind[UserService] to mock[UserService]
          binding to new UserController
        }
      }))

  "Application" should {

    "send 404 on a bad request" in new WithApplication(app) {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication(app) {
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      //contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("index")
    }

    "render login page" in new WithApplication(app) {
      val page = route(FakeRequest(GET, "/login")).get
      status(page) must equalTo(OK)
      contentType(page) must beSome.which(_ == "text/html")
      contentAsString(page) must contain("login")

    }

    "have login route" in new WithApplication(app) {
      val page = route(FakeRequest(POST, "/login")
        .withFormUrlEncodedBody("email" -> "test@test.com", "password" -> "test"))
        .get
      status(page) must equalTo(OK)
    }

  }
}
