import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "AnalysisDashboard"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
      "mysql" % "mysql-connector-java" % "5.1.18"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(

		//This line is needed to load the .json files correctly for Play framework tests
		//See http://journal.michaelahlers.org/2013/01/play-framework-and-testing-resources.html
		//console: show test:resource-directory will show the right folder now
		
      	resourceDirectory in Test <<= (baseDirectory) apply  {(baseDir: File) => baseDir / "test"}  
    )
}
