name := """oauthly"""
organization := "com.garworks"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

resolvers += Resolver.mavenLocal
resolvers += Resolver.jcenterRepo

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  guice,
  ws,
  javaJdbc,
  ehcache,
  javaWs,
  evolutions,
  "mysql" % "mysql-connector-java" % "5.1.39"
)


libraryDependencies ++= Seq(
  "org.mindrot"%"jbcrypt"%"0.4",
  "com.auth0"%"java-jwt"%"2.2.0",
  "com.typesafe.play" %% "play-mailer" % "6.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "6.0.1"
)

