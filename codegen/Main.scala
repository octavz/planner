import java.io.File
import slick.codegen.SourceCodeGenerator

import mojolly.inflector.Inflector
import com.typesafe.config.ConfigFactory
import slick.driver.PostgresDriver
import scala.concurrent.ExecutionContext.Implicits.global

object SlickGenerator {
  val conf = ConfigFactory.parseFile(new File("conf/application.conf"))
  val dbUrl = conf.getString("slick.dbs.default.db.url")
  val dbUser = conf.getString("slick.dbs.default.db.user")
  val dbPassword = conf.getString("slick.dbs.default.db.password")
  val allowed = List("access_tokens", "actions", "auth_codes", "client_grant_types", "clients", "entity_types",
    "grant_types", "groups", "groups_users", "labels", "projects", "resources", "user_sessions", "user_statuses", "users", "verbs", "tasks")

  def main(args: Array[String]) = {
    codeGen.onSuccess {
      case gen =>
        gen.writeToFile(
          "scala.slick.driver.PostgresDriver",
          "app",
          "org.planner.db",
          "DB",
          "DB.scala"
        )
    }
  }

  def getUrl = if (dbUrl.contains("?")) s"$dbUrl&user=$dbUser&password=$dbPassword" else s"$dbUrl?user=$dbUser&password=$dbPassword"

  def getDriver = conf.getString("slick.dbs.default.db.driver")

  val db = PostgresDriver.api.Database.forURL(getUrl, driver = getDriver)
  // filter out desired tables
  val allowedTables = PostgresDriver.defaultTables.map(_.filter(t => allowed.contains(t.name.name)))
  val modelAction = PostgresDriver.createModel(Some(allowedTables))
  val modelFuture = db.run(modelAction)

  val codeGen = modelFuture.map { model =>
    new SourceCodeGenerator(model) {

      //      override def packageCode(profile: String, pkg: String, container: String = "Tables"): String = {
      //        s"""
      //package ${pkg}
      //// AUTO-GENERATED Slick data model
      ///** Stand-alone Slick data model for immediate use */
      //
      ///** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
      //trait ${container} {
      //  import play.api.db.slick.Config.driver.simple._
      //  ${indent(code)}
      //}
      //${sb.toString}
      //      """.trim()
      //      }

      //override def packageCode()

      val sb = new StringBuilder("\n\n")

      // override mapped table and class name
      override def entityName = dbTableName => Inflector.singularize(dbTableName.toCamelCase)

      override def tableName = dbTableName => dbTableName.toCamelCase

      //       add some custom import
      override def code = {
        super.code ++ sb.toString()
      }

      // override table generator
      override def Table = new Table(_) {
        //        override def definitions = Seq[Def](PlainSqlMapper, TableClass, TableValue)

        override def EntityType = new EntityType {
          override val classEnabled = true

//          override def code = {
//            val c = super.code
//            sb.append(c)
//            c
//          }
        }

        //        override def PlainSqlMapper = new PlainSqlMapper {
        //          override def enabled = true
        //        }

        override def code = {
          println("-----------------------------------------")
          println(super.code.head.toString)
          println("-----------------------------------------")
          sb.append(super.code.head.toString)
          sb.append("\n")
          super.code.tail
        }

        // disable entity class generation and mapping

        //        override def code = {
        //          if (allowed contains model.name.table) {
        //            sb.append(super.code ++ "\n")
        //          } else list()
        //        }

        // override contained column generator
        //        override def Column = new Column(_) {
        //          // use the data model member of .toLowerCase.toCamelCase
        //          // this column to change the Scala type, e.g. to a custom enum or anything else
        //          override def rawName = {
        //            val a = model.name.toCamelCase
        //
        //          }
        //
        //          //override def rawType = if(model.name == "SOME_SPECIAL_COLUMN_NAME") "MyCustomType" else super.rawType
        //        }

        //        override def ForeignKey = new ForeignKey(_) {
        //          override def rawName = "rel" ++ Inflector.singularize(referencedTable.model.name.table)
        //        }

        //        override def Index = new Index(_) {
        //
        //        }
      }
    }
  }
}

