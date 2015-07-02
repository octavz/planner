import org.planner.db._
import org.planner.util.Gen._
import org.planner.util.Time._
import org.specs2.execute.AsResult
import org.specs2.mock._
import org.specs2.mutable._
import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.{PlaySpecification, FakeApplication}
import slick.dbio.{DBIOAction, NoStream}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, _}
import scala.concurrent.duration._
import play.api.Application

trait BaseDALSpec extends PlaySpecification with Mockito with DB with Around {
  val duration = Duration.Inf

  def waitFor[T](f: Future[T]): T = Await.result(f, duration)

  def dbSync[R](a: DBIOAction[R, NoStream, Nothing]): R = waitFor(db.run(a))

  def testApp: Application = {
    new GuiceApplicationBuilder()
      .configure(Map(
      "slick.dbs.default.driver" -> "slick.driver.H2Driver$",
      "slick.dbs.default.db.driver" -> "org.h2.Driver",
      "slick.dbs.default.db.url" -> "jdbc:h2:mem:test;DATABASE_TO_UPPER=false;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"))
      .build()
  }

  val config = DatabaseConfigProvider.get[JdbcProfile](testApp)
  val profile = config.driver
  val db = config.db

  import config.driver.api._

  val testUser = User(id = guid, login = guid, providerToken = Some("test"), created = now, updated = now, lastLogin = None, password = guid, nick = guid, userId = None, groupId = None)

  val testProject = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None, created = now, updated = now)

  val testGroup = Group(id = guid, projectId = testProject.id, name = p.name, created = now, updated = now, userId = testUser.id, groupId = None)

  def randUser = User(id = guid, login = guid, providerToken = Some("test"), created = now, updated = now, lastLogin = None, password = guid, nick = guid, userId = None, groupId = None)

  def randProject(uid: String) = Project(id = guid, userId = uid, name = guid, description = guido, parentId = None, created = now, updated = now)

  def randGroup(p: Project) = Group(id = guid, projectId = p.id, name = p.name, created = now, updated = now, userId = testUser.id, groupId = None)

  def createTestDB = {
    try {
      try {
        println(schema.createStatements.toList)

      } catch {
        case e: Throwable =>
        //            println("Failed to drop")
      }
      schema.createStatements
      val a = (for {
        _ <- UserStatuses += UserStatus(0, Some("active"))
        _ <- UserStatuses += UserStatus(1, Some("inactive"))
        _ <- UserStatuses += UserStatus(2, Some("pending"))
        _ <- Users += (testUser) //add test user
        _ <- Projects += testProject // add a test project
        _ <- Groups += testGroup //add test group
        _ <- GroupsUsers += GroupsUser(groupId = testGroup.id, userId = testUser.id) //add test user to the test group
      } yield ()).transactionally
      db.run(a)
    } catch {
      case e: Exception =>
        failure(e.getMessage)
    }
  }

  def around[T: AsResult](t: => T) = {
    try {
      running(testApp) {
            createTestDB
//            val redis = new RedisServer(6379)
//            redis.start()
            val result = AsResult(t)
//            redis.stop()
            result
      }
    } catch {
      case e: Exception => {
        failure(e.getMessage())
      }
    }
  }

  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString
}
