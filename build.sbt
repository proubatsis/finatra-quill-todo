name := "finch-quill-todo"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core" % "0.3.0",
  "org.tpolecat"   %% "doobie-contrib-postgresql" % "0.3.0",
  "com.github.finagle" %% "finch-core" % "0.14.0",
  "com.github.finagle" %% "finch-circe" % "0.14.0",
  "io.circe" %% "circe-generic" % "0.7.0"
)
