package dreipc.plugins.development.modul

import io.wusa.Info
import io.wusa.SemverGitPlugin
import io.wusa.TagType
import io.wusa.extension.SemverGitPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Transformer
import org.gradle.kotlin.dsl.get

class SemanticVersioning : Plugin<Project> {

  override fun apply(project: Project) {
    project.plugins.apply(SemverGitPlugin::class.java)

    val semver = project.extensions["semver"] as SemverGitPluginExtension

    semver.tagType = TagType.LIGHTWEIGHT
    semver.snapshotSuffix = ""
    semver.dirtyMarker = "" // "uncommitted-files"
    semver.initialVersion = "0.0.1"

    semver.branches {
      branch {
        regex = "release/.*"
        incrementer = "NO_VERSION_INCREMENTER"
        formatter =
          Transformer { info: Info -> "${info.version.major}.${info.version.minor}.${info.version.patch}-rc.${info.count}" }
      }
      branch {
        regex = "master"
        incrementer = "NO_VERSION_INCREMENTER"
        formatter =
          Transformer { info: Info -> "${info.version.major}.${info.version.minor}.${info.version.patch}" }
      }
      branch {
        regex = "develop" // DEFAULT: [develop, feature, etc.]
        incrementer = "NO_VERSION_INCREMENTER"
        formatter =
          Transformer { info: Info -> "${info.version.major}.${info.version.minor}.${info.version.patch}-dev.${info.count}" }
      }
      branch {
        regex = ".+" // regex for the branch you want to configure, put this one last
        incrementer = "NO_VERSION_INCREMENTER"
        formatter =
          Transformer { info: Info -> "${info.version.major}.${info.version.minor}.${info.version.patch}-build.${info.count}.sha.${info.shortCommit}" }
      }
    }

    // Disable tasks due to incompatibility with the gradle configuration cache (doLast and Project usage Problem)
    // @see https://docs.gradle.org/8.0.2/userguide/configuration_cache.html#config_cache:requirements:disallowed_types
    val showInfo = project.tasks.getByName("showInfo")
    showInfo.enabled = false // due to conflict with gradle  configuration cache (doLast[]) @see

    val showVersion = project.tasks.getByName("showVersion")
    showVersion.enabled = false

    val version = System.getenv("CI_APP_VERSION") ?: semver.info.toString()
    project.version = version
    project.project.version = version
    project.allprojects.forEach { it.version = version }
  }
}
