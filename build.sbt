
organization := "vu"

version := "0.1"

name := "shapeless-weka"

scalaVersion := "2.11.5"

scalacOptions := Seq("-unchecked", "-deprecation", "-Xlint",
  "-language:implicitConversions", "-language:postfixOps",
  "-encoding", "utf8")

fork in run := true

libraryDependencies ++= Seq(
  "com.chuusai"             %  "shapeless_2.11" % "2.1.0", // https://github.com/milessabin/shapeless/wiki/Feature-overview:-shapeless-2.0.0
  "nz.ac.waikato.cms.weka"  %  "weka-stable" % "3.6.10"
)
