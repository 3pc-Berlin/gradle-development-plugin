package dreipc.plugins.development

import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DevelopmentPluginTest {

  private val pluginProject: Project = ProjectBuilder.builder().build()

  @BeforeEach
  fun setUp() {
    pluginProject.pluginManager.apply("dreipc.development")
  }

  @Test
  fun `plugin should be enabled`() {
    assertThat(pluginProject.plugins.getPlugin(DevelopmentPlugin::class.java)).isNotNull
  }

  @Test
  fun `code generation lombok plugin should be enabled`() {
    assertThat(pluginProject.plugins.hasPlugin("io.freefair.lombok")).isNotNull
  }

  @Test
  fun `test configuration should be present`() {
    assertThat(pluginProject.tasks.findByPath("test")).isNotNull
    assertThat(pluginProject.tasks.findByPath("integrationTest")).isNotNull
    assertThat(pluginProject.tasks.findByPath("e2eTest")).isNotNull
  }

  @Test
  fun `semantic versioning should be enabled and set up`() {
    assertThat(pluginProject.plugins.hasPlugin("io.wusa:semver-git-plugin")).isNotNull
    assertThat(pluginProject.version.toString()).isNotEqualTo("unspecified")
  }

  @Test
  fun `code quality plugins should be enabled`() {
    assertThat(pluginProject.plugins.hasPlugin("com.diffplug.spotless:spotless-plugin-gradle")).isNotNull
    assertThat(pluginProject.plugins.hasPlugin("net.ltgt.gradle:gradle-errorprone-plugin")).isNotNull
    assertThat(pluginProject.plugins.hasPlugin("net.ltgt.gradle:gradle-nullaway-plugin")).isNotNull
  }
}
