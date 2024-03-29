package dreipc.plugins.development.modul

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import net.ltgt.gradle.errorprone.ErrorPronePlugin
import net.ltgt.gradle.errorprone.errorprone
import net.ltgt.gradle.nullaway.NullAwayExtension
import net.ltgt.gradle.nullaway.NullAwayPlugin
import net.ltgt.gradle.nullaway.nullaway
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import java.io.File

class CodeQuality : Plugin<Project> {

  private var nullawayVersion: String = "0.10.8"
  private var errorproneCoreVersion = "2.18.0"

  override fun apply(project: Project) {
    project.plugins.apply(SpotlessPlugin::class.java)
    project.plugins.apply(ErrorPronePlugin::class.java)
    project.plugins.apply(NullAwayPlugin::class.java)

    configSpotless(project)
    configErrorProne(project)
    configNullaway(project)

    // ToDo: fix on Mac OS (M1) and re-enable
    // addGitHock()
  }

  private fun addGitHock() {
    val gitHooksDir = File(".git/hooks")
    if (!gitHooksDir.exists()) return

    val preCommitHookContent = this::class.java.classLoader.getResourceAsStream("pre-commit")
      ?.bufferedReader()
      ?.readText()
      ?: ""

    val preCommit = gitHooksDir.resolve("pre-commit")

    preCommit.writeText(preCommitHookContent, Charsets.UTF_8)
    preCommit.setExecutable(true, false)
    preCommit.setReadable(true, false)
    preCommit.setWritable(true, true)

    File("gradlew").setExecutable(true, false)
    File("gradlew.bat").setExecutable(true, false)
  }

  private fun configSpotless(project: Project) {
    val spotless = project.extensions["spotless"] as SpotlessExtension

    if (project.plugins.hasPlugin("java")) {
      with(spotless) {
        java {
          target(
            project.fileTree(".") {
              include("**/*.java")
              exclude("**/build/**")
            },
          )
          toggleOffOn()
          palantirJavaFormat()
          removeUnusedImports()
          trimTrailingWhitespace()
          endWithNewline()

          importOrder("", "java|javax", "\\#")

          replaceRegex("Remove empty lines before end of block", "\\n[\\n]+(\\s*})(?=\\n)", "\n$1")
          replaceRegex("Remove trailing empty comment lines.", "\\n\\s*\\*(\\n\\s*\\*/\\n)", "§1")
        }
      }
    }

    if (project.plugins.hasPlugin("kotlin")) {
      with(spotless) {
        kotlin {
          project.fileTree(".") {
            include("**/*.kt")
          }
          trimTrailingWhitespace()
          encoding("utf-8")

          ktlint().editorConfigOverride(mapOf("disabled_rules" to "no-wildcard-imports,filename", "indent_size" to 2))
        }
      }
    }
  }

  private fun configErrorProne(project: Project) {
    project.tasks.withType(JavaCompile::class.java) {
      options.errorprone.disableWarningsInGeneratedCode.set(true)
      options.errorprone.isEnabled.set(true)
      options.errorprone.nullaway {
        error()
        unannotatedSubPackages.add("dreipc")
      }
    }

    with(project) {
      dependencies {
        "errorprone"("com.google.errorprone:error_prone_core:$errorproneCoreVersion")
        "errorprone"("com.uber.nullaway:nullaway:$nullawayVersion")
      }
    }
  }

  private fun configNullaway(project: Project) {
    val nullaway = project.extensions["nullaway"] as NullAwayExtension
    nullaway.annotatedPackages.add("net.ltgt")
  }
}
