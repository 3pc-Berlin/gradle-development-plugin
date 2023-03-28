package dreipc.plugins.development.modul


import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.filter
import org.gradle.language.jvm.tasks.ProcessResources

/**
 * This plugin enables property expansion for all spring application files (.yml|.properties)
 *
 * gradle properties (task: gradlew properties) will be injected into the application files using @...@ annotations
 * e.g. (application.yml):
 * spring:
 *   application:
 *     name: @name@
 */
class GradlePropertyExpansion : Plugin<Project> {

    @Suppress("UnstableApiUsage") // ignore @Incubating
    override fun apply(project: Project) {
        val processResources = project.tasks.getByName("processResources") as ProcessResources

        with(processResources) {
            project.logger.info("Expand Gradle Properties")
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
            from("src/main/resources") {
                include(".*", "**/application*.yml", "**/application*.properties")
                project.properties.forEach { prop ->
                    if (prop.value != null) {
                        filter<ReplaceTokens>("tokens" to mapOf(prop.key.toString() to prop.value.toString()))
                        filter<ReplaceTokens>("tokens" to mapOf("project." + prop.key.toString() to prop.value.toString()))
                    }
                }
            }
        }
    }
}