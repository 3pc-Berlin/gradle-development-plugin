package dreipc.plugins.development.modul

import io.freefair.gradle.plugins.lombok.tasks.LombokConfig
import io.freefair.gradle.plugins.lombok.tasks.LombokTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

/**
 * Java Code Generation plugin for reduction of boilerplate code for pojo's
 * @see [Lombok Gradle](https://plugins.gradle.org/plugin/io.freefair.lombok)
 * @see [Official Lombok](https://projectlombok.org/)
 *
 * Author: Sören Räuchle
 */
class Lombok : Plugin<Project> {
  override fun apply(project: Project) {
    project.plugins.apply("io.freefair.lombok")

    project.changeLombokGroupName("code generation")
    addLombokConfig()
  }

  private fun Project.changeLombokGroupName(name: String) = afterEvaluate {
    project.tasks.withType(LombokTask::class.java) {
      group = name
    }

    this.tasks.withType(LombokConfig::class.java) {
      group = name
    }
  }

  private fun addLombokConfig() {
    val lombokConfigFile = File("lombok.config")
    if (lombokConfigFile.exists()) return

    val lombokConfigContent = this::class.java.classLoader.getResourceAsStream("lombok.config")
      ?.bufferedReader()
      ?.readText()
      ?: ""

    lombokConfigFile.writeText(lombokConfigContent, Charsets.UTF_8)
  }
}
