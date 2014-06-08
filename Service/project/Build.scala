import sbt._
import Keys._
import Tests._

object stagedBuild extends Build {
  /** main project containing main source code depending on slick and codegen project */
  lazy val mainProject = Project(
    id="main",
    base=file("."),
    settings = sharedSettings ++ Seq(
      slick <<= slickCodeGenTask // register manual sbt command
    )
  ).dependsOn( codegenProject )
  /** codegen project containing the customized code generator */
  lazy val codegenProject = Project(
    id="codegen",
    base=file("codegen"),
    settings = sharedSettings
  )

  // shared sbt config between main project and codegen project
  val sharedSettings = Project.defaultSettings ++ Seq(
    scalaVersion := "2.10.3",
    libraryDependencies ++= List(
      "com.typesafe.slick" %% "slick" % "2.0.1",
      "com.typesafe" % "config" % "1.2.0",
      "io.backchat.inflector" %% "scala-inflector" % "1.3.5"
    )
  )

  // code generation task that calls the customized code generator
  lazy val slick = TaskKey[Seq[File]]("gen-tables")
  lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
    val outputDir = (dir / "slick").getPath // place generated files in sbt's managed sources folder
    toError(r.run("SlickGenerator", cp.files, Array(outputDir), s.log))
    Seq(file(""))
  }
}

