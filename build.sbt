
organization := "vu"

version := "0.1"

name := "shapeless-weka"

scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-Xlint",
  "-language:implicitConversions", "-language:postfixOps",
  "-encoding", "utf8")

fork in run := true

libraryDependencies ++= Seq(
  "com.chuusai"             %  "shapeless_2.11" % "2.2.5", // https://github.com/milessabin/shapeless/wiki/Feature-overview:-shapeless-2.0.0
  "nz.ac.waikato.cms.weka"  %  "weka-stable" % "3.6.10"
)
