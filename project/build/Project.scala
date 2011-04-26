import sbt._
import java.util.jar.Attributes.Name._


class Project(info: ProjectInfo) extends DefaultProject(info) with ProguardProject {
  // 
  // repositories
  //

  val ScalaNLPRepo = "ScalaNLP Maven2" at "http://repo.scalanlp.org/repo"
  val OndexRepo = "ondex" at "http://ondex.rothamsted.bbsrc.ac.uk/nexus/content/groups/public"
  val scalaToolsSnapshots = "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"

  //
  // dependencies
  //

  val JLine = "jline" % "jline" % "0.9.94"
  val NetlibJava = "netlib" % "netlib-java" % "0.9.2"
  val ArpackCombo = "netlib" % "arpack-combo" % "0.1"
  val JCommon = "jfree" % "jcommon" % "1.0.16"
  val JFreeChart = "jfree" % "jfreechart" % "1.0.13"
  val XMLGraphicsCommons = "org.apache.xmlgraphics" % "xmlgraphics-commons" % "1.3.1"
  val IText = "com.lowagie" % "itext" % "2.1.5" intransitive()
  val ScalaCheck = "org.scala-tools.testing" % "scalacheck_2.9.0.RC1" % "1.8" % "test"
  val ScalaTest = "org.scalatest" % "scalatest" % "1.4.RC2" % "test"
  val JUnit = "junit" % "junit" % "4.5" % "test"

  //
  // configuration
  //
  
  override def mainClass: Option[String] = Some("scalala.ScalalaConsole")

  override def compileOptions =
    Optimise :: Deprecation ::
    target(Target.Java1_5) ::
    Unchecked ::
    super.compileOptions.toList

  override def packageOptions = ManifestAttributes(
    MAIN_CLASS -> "scalala.ScalalaConsole",
    IMPLEMENTATION_TITLE -> "Scalala",
    IMPLEMENTATION_URL -> "http://scalala.org/",
    IMPLEMENTATION_VENDOR -> "scalala.org",
    SEALED -> "true") :: Nil

  override def managedStyle = ManagedStyle.Maven

  override def packageDocsJar = defaultJarPath("-javadoc.jar")

  override def packageSrcJar = defaultJarPath("-sources.jar")

  override def packageTestSrcJar = defaultJarPath("-test-sources.jar")

  lazy val sourceArtifact = Artifact(artifactID, "src", "jar", Some("sources"), Nil, None)

  lazy val docsArtifact = Artifact(artifactID, "docs", "jar", Some("javadoc"), Nil, None)

  override def compileOrder = CompileOrder.JavaThenScala

  override def packageToPublishActions =
    super.packageToPublishActions ++ Seq(packageDocs, packageSrc, packageTestSrc)
  
  override def allDependencyJars = (
    super.allDependencyJars +++ 
    Path.fromFile(buildScalaInstance.compilerJar) +++ 
    Path.fromFile(buildScalaInstance.libraryJar)
  )

  override def proguardOptions = List(
    "-keep class scalala.** { *; }",
    "-keep class org.jfree.** { *; }",
    proguardKeepMain("scalala.ScalalaConsole$"),
    proguardKeepMain("scala.tools.nsc.MainGenericRunner"),
    "-dontoptimize",
    "-dontobfuscate", 
    proguardKeepLimitedSerializability,
    proguardKeepAllScala,
    "-keep class ch.epfl.** { *; }",
    "-keep interface scala.ScalaObject"
  )

  //
  // publishing
  //  
  val publishToRepoName = "Sonatype Nexus Repository Manager"
  val publishTo = {
    val repoUrl = "http://nexus.scala-tools.org/content/repositories/" +
      (if (version.toString.endsWith("-SNAPSHOT")) "snapshots" else "releases")
    publishToRepoName at repoUrl
  }

  lazy val publishUser = system[String]("build.publish.user")
  lazy val publishPassword = system[String]("build.publish.password")

  (publishUser.get, publishPassword.get) match {
    case (Some(u), Some(p)) =>
      Credentials.add(publishToRepoName, "nexus.scala-tools.org", u, p)
    case _ =>
      Credentials(Path.userHome / ".ivy2" / ".credentials", log)
  }
}

