import controllers._

import org.planner.controllers.{UserController, MainController}
import org.planner.modules.core.UserModuleComponent
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
import org.planner.modules._

import scala.concurrent._
import scala.concurrent.duration._
import scalaoauth2.provider.GrantHandlerResult

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class UserAppSpec extends Specification with Mockito {

  def waitFor[T](f: Future[T], duration: FiniteDuration = 1000 milli)(implicit ec: ExecutionContext): T = Await.result(f, duration)

  def app(m: UserModuleComponent = mock[UserModuleComponent]) = FakeApplication(
    additionalConfiguration = Map(
      "evolutionplugin" -> "disabled",
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"),
    withoutPlugins = Seq("com.typesafe.plugin.RedisPlugin"),
    withGlobal = Some(
      new GlobalSettings {
        override def getControllerInstance[A](clazz: Class[A]) = clazz match {
          case c if c.isAssignableFrom(classOf[UserController]) => new UserController with UserModuleComponent {
            override val dalAuth: Oauth2DAL = mock[Oauth2DAL]
            override val userModule = m.userModule.asInstanceOf[this.UserModule]
          }.asInstanceOf[A]
          case _ => super.getControllerInstance(clazz)
        }
      }))


  def newComp = new UserModuleComponent {
    override val userModule: UserModule = mock[UserModule]
  }

  "Application" should {

    "have external static files attached to root" in new WithApplication(app()) {
      route(FakeRequest(GET, "/docs/index.html")) must beSome
    }

    "render login page" in new WithApplication(app()) {
      val page = route(FakeRequest(GET, "/login")).get
      status(page) must equalTo(OK)
      contentType(page) must beSome.which(_ == "text/html")
      contentAsString(page) must contain("login")

    }

    "have login route" in {
      val service = newComp
      running(app(service)) {
        service.userModule.login(any) returns Future.successful(Right(GrantHandlerResult(tokenType = "1", accessToken = "at", expiresIn = None, refreshToken = None, scope = None)))
        val page = route(FakeRequest(POST, "/login")
          .withFormUrlEncodedBody("email" -> "test@test.com", "password" -> "12345"))
        page must beSome
        waitFor(page.get)
        status(page.get) must equalTo(SEE_OTHER)
      }
    }

    "have register route" in {
      val service = newComp
      service.userModule.registerUser(any[RegisterDTO]) answers (u => result(u.asInstanceOf[RegisterDTO]))
      running(app(service)) {
        val page = route(FakeRequest(POST, "/api/register")
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
      val service = newComp
      service.userModule.addGroup(any[GroupDTO]) answers {
        u =>
          val dto = u.asInstanceOf[GroupDTO].copy(id = guido)
          result(dto)
      }
      running(app(service)) {
        val page = route(FakeRequest(POST, "/api/group")
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
