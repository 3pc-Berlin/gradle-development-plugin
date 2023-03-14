package dreipc.plugins.development

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.testing.jacoco.plugins.JacocoPlugin
import org.gradle.testing.jacoco.tasks.JacocoReport

class Testing : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply(JacocoPlugin::class.java)

        configUnitTest(project)
        registerIntegrationTest(project)
        registerEndToEndTest(project)

        basicTestSettings(project)


        project.codeCoverage()
    }

    private fun basicTestSettings(project: Project) {
        project.tasks.withType(Test::class.java).configureEach {
            group = "verification"

            useJUnitPlatform()

            minHeapSize = "512m"
            maxHeapSize = "2048m"

            systemProperty("junit.extensions.autodetection.enabled", "true")
            systemProperty("junit.jupiter.execution.parallel.enabled", "true")

            testLogging {
                events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
                showStandardStreams = true
            }
        }
    }

    private fun configUnitTest(project: Project) {
        val test = project.tasks.getByName("test") as Test
        test.systemProperty("spring.profiles.active", "test")
        test.filter {
            excludeTestsMatching("*IntegrationTest")
            excludeTestsMatching("*E2ETest")
        }
    }

    private fun registerIntegrationTest(project: Project) {
        project.tasks.register("integrationTest", Test::class.java) {
            description = "Execute integration tests."
            systemProperty("spring.profiles.active", "integrationTest")

            filter {
                includeTestsMatching("*IntegrationTest")
                excludeTestsMatching("*E2ETest")
            }
        }
    }

    private fun registerEndToEndTest(project: Project) {
        project.tasks.register("e2eTest", Test::class.java) {
            description = "Execute End-to-End (E2E) tests using testcontainers (docker)."
            systemProperty("spring.profiles.active", "e2eTest")

            filter {
                includeTestsMatching("*E2ETest")
                excludeTestsMatching("*IntegrationTest")
            }
        }
    }

    private fun Project.codeCoverage() = afterEvaluate {

        val reportTask = this.tasks.getByName("jacocoTestReport") as JacocoReport
        reportTask.reports {
            csv.required.set(false)
            html.required.set(false)
            xml.required.set(true) // CI/CD Jenkins using xml
        }

        val excludedFiles = listOf(
            "**/google/**", "**/proto/**", // Google Protobuf Code generated Classes
            "**/models/**", "**/model/**",
            "**/config/*",
            "**/exceptions/*", "**/exception/*",
            "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*", "**/*Test*.*", "android/**/*.*"
        )

        val javaDirectories = fileTree(
            "${project.buildDir}/intermediates/classes/debug"
        ) { exclude(excludedFiles) }

        val kotlinDirectories = fileTree(
            "${project.buildDir}/tmp/kotlin-classes/debug"
        ) { exclude(excludedFiles) }

        val coverageSrcDirectories = listOf(
            "src/main/java",
        )

        reportTask.classDirectories.setFrom(files(javaDirectories, kotlinDirectories))
        reportTask.additionalClassDirs.setFrom(files(coverageSrcDirectories))
        reportTask.sourceDirectories.setFrom(files(coverageSrcDirectories))

        reportTask.executionData.setFrom(
            files("${project.buildDir}/jacoco/*.exec")
        )
    }
}