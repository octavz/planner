resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases"
resolvers += "java-net" at "http://download.java.net/maven/2"
resolvers += "Sedis Repo" at "http://pk11-scratch.googlecode.com/svn/trunk"
resolvers += "Clojars " at "http://clojars.org/repo"
resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"


libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.typesafe" % "config" % "1.3.0",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.1",
  "io.backchat.inflector" %% "scala-inflector" % "1.3.5"
)
