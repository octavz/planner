import org.planner.dal.impl.{SlickUserDAL}
import org.planner.db._
import org.specs2.execute.AsResult
import org.specs2.mock._
import org.specs2.mutable._
import org.specs2.specification.AroundExample
import play.api.test.{FakeApplication, WithApplication}
import scaldi.{Injectable}
import scala.concurrent._
import scala.concurrent.duration._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.test.Helpers._
import scala.util.Try

/**
 * Created by Octav on 6/30/2014.
 */
trait BaseDALSpec extends Specification with Mockito with Injectable with DB with AroundExample {
  val duration = Duration.Inf

  def around[T: AsResult](t: => T) = {
    try {
      implicit val testApp = FakeApplication(
        additionalConfiguration = Map("db.default.driver" -> "org.h2.Driver", "db.default.url" -> "jdbc:h2:mem:play;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"))
      running(testApp) {
        DB.withSession {
          implicit s: Session =>
            if (s.metaData.getDriverName.toLowerCase.contains("h2")) {
              println("Test database")
              Try(ddl.drop)
              ddl.create
              UserStatuses.insert(UserStatus(0, Some("active")))
              UserStatuses.insert(UserStatus(1, Some("inactive")))
            }
        }
      }
      val result = AsResult(t)
      result
    } catch {
      case e: Exception =>
        failure(e.getMessage)
    }
  }

  def genString(size: Int): String = (for (i <- 1 to size) yield "a").mkString
}
