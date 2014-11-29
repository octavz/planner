import sbt._

//import play.Project._

import Keys._

//import play.Play.autoImport._


object AppBuild extends Build {
  val appName = "PlannerService"

  val appVersion = "0.1"

  val appResolvers = Seq(
    "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases",
    "java-net" at "http://download.java.net/maven/2",
    "Sedis Repo" at "http://pk11-scratch.googlecode.com/svn/trunk",
    "Clojars " at "http://clojars.org/repo",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/")

  val appDependencies = Seq(
    "com.typesafe.play" %% "play-cache" % "2.3.6",
    "com.typesafe.play" %% "play-jdbc" % "2.3.6",
    "postgresql" % "postgresql" % "9.3-1102.jdbc41",
    "com.typesafe.play" %% "play-slick % "0.8.0",
    "com.nulab-inc" %% "play2-oauth2-provider" % "0.11.0",
    "org.mockito" % "mockito-all" % "1.10.10",
    //"com.wix" %% "accord-core" % "0.4-SNAPSHOT",
    "com.wordnik" %% "swagger-play2" % "1.3.10",
    "net.sourceforge.htmlunit" % "htmlunit" % "2.15" % "test",
    "com.github.nscala-time" %% "nscala-time" % "1.4.0",
    "com.livestream" %% "scredis" % "2.0.5",
    "redis.embedded" % "embedded-redis" % "0.3"
  )

  lazy val main = Project(appName, file(".")).dependsOn(gen).enablePlugins(play.PlayScala).settings(
    scalaVersion := "2.11.4",
    libraryDependencies ++= appDependencies,
    resolvers ++= appResolvers,
    slick <<= slickCodeGenTask // register manual sbt command
  )

  lazy val gen = Project(
    id = "codegen",
    base = file("codegen")
  ).settings(
      resolvers ++= appResolvers,
      scalaVersion := "2.11.4",
      libraryDependencies ++= Seq(
        "com.typesafe.slick" %% "slick" % "2.1.0",
        "com.typesafe" % "config" % "1.2.1",
        "com.typesafe.slick" %% "slick-codegen" % "2.1.0",
        "io.backchat.inflector" %% "scala-inflector" % "1.3.5"
      )
    )

  //play.Project.playScalaSettings
  // code generation task that calls the customized code generator
  lazy val slick = TaskKey[Seq[File]]("gen-tables")

  lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
    val outputDir = (dir / "slick").getPath // place generated files in sbt's managed sources folder
    toError(r.run("SlickGenerator", cp.files, Array(outputDir), s.log))
    Seq(file(""))
  }
}

