package dreipc.plugins.development.modul


import org.apache.tools.ant.filters.*
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

    inline fun <reified C> Project.configure(name: String, configuration: C.() -> Unit) {
        (this.tasks.getByName(name) as C).configuration()
    }

    @Suppress("UnstableApiUsage") // ignore @Incubating
    override fun apply(project: Project) {
//        val processResources = project.tasks.getByName("processResources") as ProcessResources

        project.configure<ProcessResources>("processResources") {
            from("src/main/resources")
            include("**/application*.yml", "**/application*.properties")
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
            project.properties.forEach { prop ->
                if (prop.value != null) {
                    filter<ReplaceTokens>("tokens" to mapOf(prop.key.toString() to prop.value.toString()))
                    filter<ReplaceTokens>("tokens" to mapOf("project." + prop.key.toString() to prop.value.toString()))
                }
            }
        }
    }
//            tasks.withType<ProcessResources> {
//                from
//            }
//        }
//            tasks{
//                processResources{
//
//                }
//            }
//            task<processResources{
//
//            }
//        }
//        with(processResources) {
//            project.logger.info("Expand Gradle Properties")
//
//            from("src/main/resources")
//            include("**/application*.yml", "**/application*.properties")
//            duplicatesStrategy = DuplicatesStrategy.INCLUDE
//            project.properties.forEach { prop ->
//                if (prop.value != null) {
//                    filter<ReplaceTokens>("tokens" to mapOf(prop.key.toString() to prop.value.toString()))
//                    filter<ReplaceTokens>("tokens" to mapOf("project." + prop.key.toString() to prop.value.toString()))
//                }
//            }
//            doLast{
//                val semver = project.extensions["semver"] as SemverGitPluginExtension
//                val version = System.getenv("CI_APP_VERSION") ?: semver.info.toString()
//                System.out.println("Version now: ${version}")
//
//                filter<ReplaceTokens>("tokens" to mapOf("project." + prop.key.toString() to prop.value.toString()))
//
//            }
//        }
//    }
}