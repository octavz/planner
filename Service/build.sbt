import de.johoop.jacoco4sbt._
import JacocoPlugin._

name := "PlannerService"

version := "1.0-SNAPSHOT"

resolvers += "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/"

resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Sedis Repo" at "http://pk11-scratch.googlecode.com/svn/trunk"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-cache" % "2.2.1",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "com.typesafe.play" %% "play-slick" % "0.6.0.1",
  "com.nulab-inc" %% "play2-oauth2-provider" % "0.3.0",
  "org.mockito" % "mockito-all" % "1.9.5",
  "com.wix" %% "accord-core" % "0.4-SNAPSHOT",
  "com.typesafe" %% "play-plugins-redis" % "2.2.0",
  "com.wordnik" %% "swagger-play2" % "1.3.1",
  "org.scaldi" %% "scaldi" % "0.3.2",
  "org.scaldi" %% "scaldi-play" % "0.3.3",
  "com.github.nscala-time" %% "nscala-time" % "1.2.0" )

play.Project.playScalaSettings

javaOptions in Test += "-Dconfig.file=" + Option(System.getProperty("config.file")).getOrElse("conf/application.conf")

jacoco.settings

parallelExecution in jacoco.Config := false


jacoco.excludes in jacoco.Config := Seq("controllers.javascript.*", "db.*", "controllers.ref.*", "default", "controllers.routes", "controllers.routes.*","*Routes*", "*routes*" )

parallelExecution in Test := false

