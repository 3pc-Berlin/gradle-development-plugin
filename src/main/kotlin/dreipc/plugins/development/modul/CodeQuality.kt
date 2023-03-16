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

    private var nullawayVersion : String = "0.10.8"
    private var errorproneCoreVersion = "2.18.0"
    private var klintVersion = "0.48.2"
    override fun apply(project: Project) {

        project.plugins.apply(SpotlessPlugin::class.java)
        project.plugins.apply(ErrorPronePlugin::class.java)
        project.plugins.apply(NullAwayPlugin::class.java)

        configSpotless(project)
        configErrorProne(project)
        configNullaway(project)

        addGitHock()
    }

    private fun addGitHock() {
        val gitHooksDir = File(".git/hooks")
        if(!gitHooksDir.exists()) return

        val preCommitHookContent = this::class.java.classLoader.getResourceAsStream("pre-commit")
            ?.bufferedReader()
            ?.readText()
            ?: ""

        gitHooksDir.resolve("pre-commit").writeText(preCommitHookContent, Charsets.UTF_8)
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

                replaceRegex("Remove empty lines before end of block", "\\n[\\n]+(\\s*})(?=\\n)", "\n$1")
                replaceRegex("Remove trailing empty comment lines.", "\\n\\s*\\*(\\n\\s*\\*/\\n)", "§1")
            }
            kotlin {
                project.fileTree(".") {
                    include("**/*.kt")
                }
                trimTrailingWhitespace()
                ktlint(klintVersion)
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
                "errorprone"("com.google.errorprone:error_prone_core:$errorproneCoreVersion")
                "errorprone"("com.uber.nullaway:nullaway:$nullawayVersion")
            }
        }
    }

    private fun configNullaway(project: Project){
        val nullaway = project.extensions["nullaway"] as NullAwayExtension
        nullaway.annotatedPackages.add("net.ltgt")
    }
}