
name := "AKKA"

version := "1.0"

scalaVersion := "2.11.8"

//chapter4
//libraryDependencies ++= Seq(
//  "com.typesafe.akka"           %% "akka-actor"       % "2.4.0",
//  "com.typesafe.akka"           %% "akka-persistence" % "2.4.0",
//  "org.iq80.leveldb"            % "leveldb"           % "0.7",
//  "org.fusesource.leveldbjni"   % "leveldbjni-all"    % "1.8",
//  "com.typesafe.akka" %% "akka-persistence-query-experimental" % "2.4.0",
//  "com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0")

//chapter 5
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.akka" %% "akka-remote" % "2.4.0",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.0",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.0",
  "com.typesafe.akka" %% "akka-cluster-sharding" % "2.4.0",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.0",
  "com.typesafe.akka" %% "akka-contrib" % "2.4.0",
  "org.iq80.leveldb" % "leveldb" % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8")