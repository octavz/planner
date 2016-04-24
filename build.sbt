scalacOptions ++= Seq("-feature")

net.virtualvoid.sbt.graph.Plugin.graphSettings

resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases"
resolvers += "java-net" at "http://download.java.net/maven/2"
resolvers += "Sedis Repo" at "http://pk11-scratch.googlecode.com/svn/trunk"
resolvers += "Clojars " at "http://clojars.org/repo"
resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  specs2 % Test,
  evolutions,
  filters,
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.typesafe.play" %% "play-cache" % "2.5.2",
  "com.typesafe.play" %% "play-jdbc" % "2.5.2",
  "com.typesafe.play" %% "play-json" % "2.5.2",
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.nulab-inc" %% "play2-oauth2-provider" % "0.15.0",
  "com.wordnik" %% "swagger-core" % "1.3.12",
  "com.wordnik" %% "swagger-jaxrs" % "1.3.12",
  "net.sourceforge.htmlunit" % "htmlunit" % "2.17" % "test",
  "com.github.nscala-time" %% "nscala-time" % "2.0.0",
  "com.livestream" %% "scredis" % "2.0.6",
  "com.h2database" % "h2" % "1.4.187",
//  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.2",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.1"
)
