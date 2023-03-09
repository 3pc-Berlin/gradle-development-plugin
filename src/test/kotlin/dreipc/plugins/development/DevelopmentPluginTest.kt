package dreipc.plugins.development

import dreipc.plugins.development.DevPlugin
import org.assertj.core.api.Assertions.assertThat
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class DevPluginTest {

    @Test
    fun shouldLoadPlugin() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("dreipc")

        assertThat(project.plugins.getPlugin(DevPlugin::class.java)).isNotNull()
    }
}
