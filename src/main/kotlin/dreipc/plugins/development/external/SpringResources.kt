package dreipc.plugins.development.external

import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.filter
import org.gradle.language.jvm.tasks.ProcessResources

class SpringResources : Plugin<Project> {
    @Suppress("UnstableApiUsage") // ignore @Incubating
    override fun apply(project: Project) {
        val processResources = project.tasks.getByName("processResources") as ProcessResources
        processResources.from("src/main/resources")
        processResources.include("**/application*.yml", "**/application*.properties")
        processResources.duplicatesStrategy = DuplicatesStrategy.INCLUDE

        project.properties.forEach { prop ->
            val tokens = mapOf(prop.key.toString() to prop.value.toString())
            processResources.filter<ReplaceTokens>("tokens" to tokens)
        }
    }
}