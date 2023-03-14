package dreipc.plugins.development.external

import io.freefair.gradle.plugins.lombok.LombokPlugin
import io.freefair.gradle.plugins.lombok.tasks.LombokConfig
import io.freefair.gradle.plugins.lombok.tasks.LombokTask
import org.gradle.api.Plugin
import org.gradle.api.Project


class Lombok : Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply(LombokPlugin::class.java)

        project.changeLombokGroupName("code generation")
    }

    private fun  Project.changeLombokGroupName(name : String) = afterEvaluate {
        project.tasks.withType(LombokTask::class.java) {
            group = name
        }

        this.tasks.withType(LombokConfig::class.java) {
            group = name
        }
    }
}