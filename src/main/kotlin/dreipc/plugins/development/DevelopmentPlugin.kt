package dreipc.dev

import groovy.util.logging.Slf4j
import io.freefair.gradle.plugins.lombok.LombokPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.impldep.com.google.common.collect.ImmutableMap
import org.gradle.testfixtures.ProjectBuilder
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

@Slf4j
class DevPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val project = ProjectBuilder.builder().build()
        if(!target.plugins.hasPlugin("java")) {
            project.pluginManager.apply("java")
        }
        project.pluginManager.apply(LombokPlugin::class.java)

        val resourceDirectory = File("src/main/resources")
        getGradleScripts(resourceDirectory)
            .peek { script -> System.out.println("load : " + script.fileName) }
            .forEach { script -> project.apply(ImmutableMap.of("from", script.toAbsolutePath())) }
    }

    fun getGradleScripts(directory: File): Stream<Path> {
        return Files.walk(directory.toPath(), 1).filter { file -> file.toAbsolutePath().toString().endsWith("gradle") }
    }
}