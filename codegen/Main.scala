import java.io.File
import scala.slick.codegen.SourceCodeGenerator
import scala.slick.jdbc.meta.createModel
import scala.slick.driver.PostgresDriver
import mojolly.inflector.Inflector
import com.typesafe.config.ConfigFactory

/**
 * This customizes the Slick code generator. We only do simple name mappings.
 * For a more advanced example see https://github.com/cvogt/slick-presentation/tree/scala-exchange-2013
 */
object SlickGenerator {
  val conf = ConfigFactory.parseFile(new File("conf/application.conf"))
  val dbUrl = conf.getString("db.default.url")
  val dbUser = conf.getString("db.default.user")
  val dbPassword = conf.getString("db.default.password")
  val allowed = List("access_tokens", "actions", "auth_codes", "client_grant_type", "clients", "entity_types",
    "grant_types", "groups", "groups_users", "labels", "projects", "resources", "user_sessions", "user_statuses", "users", "verbs", "tasks")

  def main(args: Array[String]) = {
    codeGen.writeToFile(
      "scala.slick.driver.PostgresDriver",
      "app",
      "org.planner.db",
      "DB",
      "DB.scala"
    )
  }

  def getUrl = if (dbUrl.contains("?")) s"$dbUrl&user=$dbUser&password=$dbPassword" else s"$dbUrl?user=$dbUser&password=$dbPassword"
  def getDriver = conf.getString("db.default.driver")
  val db = PostgresDriver.simple.Database.forURL(getUrl, driver = getDriver)
  // filter out desired tables
  val model = db.withSession {
    implicit session =>
      val tables = PostgresDriver.defaultTables.filter(t => allowed.contains(t.name.name))
      createModel(tables, PostgresDriver)
  }
  val codeGen = new SourceCodeGenerator(model) {

    override def packageCode(profile: String, pkg: String, container: String = "Tables"): String = {
      s"""
package ${pkg}
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait ${container} {
  import play.api.db.slick.Config.driver.simple._
  ${indent(code)}
}
${sb.toString}
      """.trim()
    }

    val sb = new StringBuilder

    // override mapped table and class name
    override def entityName = dbTableName => Inflector.singularize(dbTableName.toCamelCase)

    override def tableName = dbTableName => dbTableName.toCamelCase

    // add some custom import
    //override def code = "  "+ "\n" + super.code

    // override table generator
    override def Table = new Table(_) {
      override def definitions = Seq[Def](PlainSqlMapper, TableClass, TableValue)

      override def PlainSqlMapper = new PlainSqlMapper {
        override def enabled = true
      }

      // disable entity class generation and mapping

      override def code = {
        if (allowed contains model.name.table) {
          sb.append(EntityType.code ++ "\n")
          super.code
        } else List()
      }

      // override contained column generator
      override def Column = new Column(_) {
        // use the data model member of .toLowerCase.toCamelCase
        // this column to change the Scala type, e.g. to a custom enum or anything else
        override def rawName = {
          val a = model.name.toCamelCase
          s"${a.head.toLower}${a.tail}"
        }

        //override def rawType = if(model.name == "SOME_SPECIAL_COLUMN_NAME") "MyCustomType" else super.rawType
      }

      override def ForeignKey = new ForeignKey(_) {
        override def rawName = "rel" ++ Inflector.singularize(referencedTable.model.name.table)
      }

      override def Index = new Index(_) {

      }
    }
  }
}

