import sbt._
import play.Project._
import Keys._
//import play.Play.autoImport._

//resolvers += "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/"
//
//resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
// resolvers += "Sedis Repo" at "http://pk11-scratch.googlecode.com/svn/trunk"

object AppBuild extends Build {
  val appName = "PlannerService"

  val appVersion = "0.1-SNAPSHOT"


  val appDependencies = Seq(
    "com.typesafe.play" %% "play-cache" % "2.2.3",
    "com.typesafe.play" %% "play-jdbc" % "2.2.3",
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
    "com.typesafe.play" %% "play-slick" % "0.6.0.1",
    "com.nulab-inc" %% "play2-oauth2-provider" % "0.7.1",
    "org.mockito" % "mockito-all" % "1.9.5",
    //"com.wix" %% "accord-core" % "0.4-SNAPSHOT",
    "com.typesafe" %% "play-plugins-redis" % "2.2.1",
    "org.scaldi" %% "scaldi" % "0.3.2",
    "org.scaldi" %% "scaldi-play" % "0.3.3",
    "com.wordnik" %% "swagger-play2" % "1.3.5",
    "net.sourceforge.htmlunit" % "htmlunit" % "2.14" % "test",
    "com.github.nscala-time" %% "nscala-time" % "1.2.0"
  )

  lazy val main = play.Project(appName, appVersion, appDependencies).dependsOn(codegenProject)./*enablePlugins(play.PlayScala).*/settings(
    scalaVersion := "2.10.4",
    slick <<= slickCodeGenTask // register manual sbt command
  )

  lazy val codegenProject = Project(
    id = "codegen",
    base = file("codegen")
  ).settings(
      scalaVersion := "2.10.4",
      libraryDependencies ++= Seq(
        "com.typesafe.slick" %% "slick" % "2.0.2",
        "com.typesafe" % "config" % "1.2.1",
        "io.backchat.inflector" %% "scala-inflector" % "1.3.5"
      )
    )
  play.Project.playScalaSettings
  // code generation task that calls the customized code generator
  lazy val slick = TaskKey[Seq[File]]("gen-tables")

  lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
    val outputDir = (dir / "slick").getPath // place generated files in sbt's managed sources folder
    toError(r.run("SlickGenerator", cp.files, Array(outputDir), s.log))
    Seq(file(""))
  }
}

