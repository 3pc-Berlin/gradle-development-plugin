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

// ToDo: Add GitHook for code formatting!
class CodeQuality : Plugin<Project> {

    var NULLIFY_VERSION : String = "0.10.8"
    var ERRORPRONE_CORE_VERSION = "2.18.0"
    override fun apply(project: Project) {

        project.plugins.apply(SpotlessPlugin::class.java)
        project.plugins.apply(ErrorPronePlugin::class.java)
        project.plugins.apply(NullAwayPlugin::class.java)

        configSpotless(project)
        configErrorProne(project)
        configNullaway(project)
    }

    private fun configSpotless(project: Project) {
        val spotless = project.extensions["spotless"] as SpotlessExtension

        with(spotless) {
            java {
                project.fileTree(".") {
                    include("**/*.java")
                    exclude("**/build/**", "**/build-*/**")
                }
                toggleOffOn()
                palantirJavaFormat()
                removeUnusedImports()
                trimTrailingWhitespace()
                endWithNewline()
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

        with(project){
            dependencies {
                "errorprone"("com.google.errorprone:error_prone_core:$ERRORPRONE_CORE_VERSION")
                "errorprone"("com.uber.nullaway:nullaway:$NULLIFY_VERSION")
            }
        }
    }

    private fun configNullaway(project: Project){
        val nullaway = project.extensions["nullaway"] as NullAwayExtension
        nullaway.annotatedPackages.add("net.ltgt")
    }
}