import org.planner.util.Gen._
import org.planner.util.Time._
import org.planner.dal.impl.{SlickUserDAL}
import org.planner.db._
import org.specs2.execute.AsResult
import org.specs2.mock._
import org.specs2.mutable._
import org.specs2.specification.AroundExample
import play.api.test.{FakeApplication}
import redis.embedded.RedisServer
import scaldi.{Injectable}
import scala.concurrent.duration._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.test.Helpers._

trait BaseDALSpec extends Specification with Mockito with Injectable with DB with AroundExample {
  val duration = Duration.Inf

  def testApp = FakeApplication(additionalConfiguration = Map(
    "evolutionplugin" -> "disabled",
    "db.default.driver" -> "org.h2.Driver",
    "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"))

  val testUser = User(id = guid, login = guid, providerToken = Some("test"), created = now, updated = now, lastLogin = None, password = guid, nick = guid, userId = None, groupId = None)

  val testProject = Project(id = guid, userId = testUser.id, name = guid, description = guido, parentId = None, created = now, updated = now)

  val testGroup = Group(id = guid, projectId = testProject.id, name = p.name, created = now, updated = now, userId = testUser.id, groupId = None)

  def createTestDB(implicit s: Session) = {
    try {
      if (s.metaData.getDriverName.toLowerCase.contains("h2")) {
//        println("Test database")
        try {
          ddl.drop
        } catch {
          case e: Throwable =>
//            println("Failed to drop")
        }
        ddl.create
        UserStatuses.insert(UserStatus(0, Some("active")))
        UserStatuses.insert(UserStatus(1, Some("inactive")))
        UserStatuses.insert(UserStatus(2, Some("pending")))
        Users.insert(testUser) //add test user
        Projects.insert(testProject) // add a test project
        Groups.insert(testGroup) //add test group
        GroupsUsers.insert(GroupsUser(groupId = testGroup.id, userId = testUser.id)) //add test user to the test group
      }
    } catch {
      case e: Exception =>
        failure(e.getMessage)
    }
  }

  def around[T: AsResult](t: => T) = {
    try {
      running(testApp) {
        DB(testApp).withSession {
          implicit s: Session =>
            createTestDB
            val redis = new RedisServer(6379)
            redis.start()
            val result = AsResult(t)
            redis.stop()
            result
        }
      }
    } catch {
      case e: Exception => {
        failure(e.getMessage())
      }
    }
  }

  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString
}
