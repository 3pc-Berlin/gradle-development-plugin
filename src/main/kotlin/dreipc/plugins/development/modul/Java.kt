package dreipc.plugins.development.modul

import com.bmuschko.gradle.docker.shaded.org.apache.commons.lang3.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

class Java : Plugin<Project> {
  override fun apply(project: Project) {
    if (!project.plugins.hasPlugin("java")) {
      project.plugins.apply("java")
    }

    project.plugins.withType(JavaPlugin::class.java) {
      val javaPluginExtension = project.extensions.get("java") as JavaPluginExtension
      javaPluginExtension.setSourceCompatibility(JavaVersion.JAVA_17)
    }

    // ADD Default  3pc Maven Repository
    project.afterEvaluate {
      repositories {
        mavenCentral()
        mavenLocal()
        maven(url = "https://nexus.3pc.de/repository/maven-group/")
      }
    }
  }
}
