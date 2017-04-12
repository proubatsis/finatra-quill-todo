name := "finch-quill-todo"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "io.getquill" % "quill_2.11" % "1.1.0",
  "com.github.finagle" %% "finch-core" % "0.14.0",
  "com.github.finagle" %% "finch-circe" % "0.14.0",
  "io.circe" %% "circe-generic" % "0.7.0"
)
