import sbt._

//import play.Project._

import sbt.Keys._

//import play.Play.autoImport._


object AppBuild extends Build {
  val appName = "PlannerService"

  val appVersion = "0.2"


  lazy val main = Project(appName, file(".")).dependsOn(gen).enablePlugins(play.sbt.Play).settings(
    scalaVersion := "2.11.7",
//    Keys.fork in (Test) := true,
//    javaOptions in (Test) += "-Xdebug",
//    javaOptions in (Test) += "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9998",
    slick <<= slickCodeGenTask // register manual sbt command
  )

  //  lazy val slick = TaskKey[Seq[File]]("gen-tables")
  //  lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
  //    val outputDir = (dir / "slick").getPath // place generated files in sbt's managed sources folder
  //  //  val url = "jdbc:h2:mem:test;INIT=runscript from 'src/main/sql/create.sql'" // connection info for a pre-populated throw-away, in-memory db for this demo, which is freshly initialized on every run
  //  val url = "jdbc:postgresql://localhost:5432/planner"
  //    val jdbcDriver = "org.postgresql.Driver"
  //    val slickDriver = "scala.slick.driver.PostegresqlDriver"
  //    val pkg = "demo"
  //    toError(r.run("scala.slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg), s.log))
  //    val fname = outputDir + "/demo/Tables.scala"
  //    Seq(file(fname))
  //  }
  lazy val gen = Project(
    id = "codegen",
    base = file("codegen")
  ).settings(
      scalaVersion := "2.11.7"
    )
  //
  //  //play.Project.playScalaSettings
  // code generation task that calls the customized code generator
  lazy val slick = TaskKey[Seq[File]]("gen-tables")

  lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
    val outputDir = (dir / "slick").getPath
    toError(r.run("SlickGenerator", cp.files, Array(outputDir), s.log))
    Seq(file(""))
  }
}

