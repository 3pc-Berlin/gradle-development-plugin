package dreipc.plugins.development.modul

import com.bmuschko.gradle.docker.tasks.AbstractDockerRemoteApiTask
import com.bmuschko.gradle.docker.tasks.RegistryCredentialsAware
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.DockerRemoveImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import dreipc.plugins.development.extension.DockerPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem.current
import java.io.File

class Docker : Plugin<Project> {

  private val DEFAULT_REPO_URL = "nexus.3pc.de"
  private val DOCKER_BASE_IMAGE = "nexus.3pc.de/java"
  private var DEFAULT_EXPOSE_PORT = 8080
  private var JAVA_OPTS_PLUGIN_DEFAULTS = "" +
    " -Djava.security.egd=file:/dev/./urandom -XX:TieredStopAtLevel=1" +
    " -Dspring.jmx.enabled=true -Dspring.config.location=classpath:/application.yml" +
    " -Djava.awt.headless=true" +
    " -Dfile.encoding=UTF-8 -DLC_CTYPE=UTF-8"

  var dockerImage = ""

  override fun apply(project: Project) {
    project.plugins.apply("com.bmuschko.docker-java-application")

    val extension = project.extensions.create("dockerConfig", DockerPluginExtension::class.java)
    val javaVersion = project.properties["sourceCompatibility"]
    extension.image.set("$DOCKER_BASE_IMAGE:$javaVersion")

    project.afterEvaluate {
      dockerImage = extension.image.get()
    }

    configureRegistryCredentials(project)
    configureDockerAPI(project)

    createDockerFileTask(project)
    buildImageTask(project)
    pushImageTask(project)
    removeImageTask(project)

    addDockerIgnore()
  }

  private fun buildImageNameTag(project: Project): String {
    return System.getenv("CI_DOCKER_NAMEONLY:${project.version}")
      ?: "$DEFAULT_REPO_URL/${project.name}:${project.version}" // allow CI to override image path (e.g. add /temp/ to the path)
  }

  private fun configureRegistryCredentials(
    project: Project,
  ) {
    val repoUrl = System.getenv("DOCKER_REPOSITORY_URL") ?: DEFAULT_REPO_URL
    val user = System.getenv("DOCKER_USERNAME") ?: System.getenv("REPO_3PC_USERNAME")
    val password = System.getenv("DOCKER_PASSWORD") ?: System.getenv("REPO_3PC_PWD")
    val email = System.getenv("DOCKER_USER_EMAIL") ?: ""

    project.tasks.withType(RegistryCredentialsAware::class.java)
      .configureEach {
        registryCredentials.url.set(repoUrl)
        registryCredentials.username.set(user)
        registryCredentials.password.set(password)
        registryCredentials.email.set(email)
      }
  }

  private fun configureDockerAPI(
    project: Project,
  ) {
    val daemonUrl = if (current().isWindows()) "tcp://127.0.0.1:2375" else "unix:///var/run/docker.sock"

    project.tasks.withType(AbstractDockerRemoteApiTask::class.java)
      .configureEach { this.url.set(daemonUrl) }
  }

  private fun createDockerFileTask(project: Project) = project.afterEvaluate {
    val portValue = System.getenv("DOCKER_EXPOSE_PORT") ?: "$DEFAULT_EXPOSE_PORT"
    val port = portValue.toInt()

    project.tasks.register("createDockerfile", Dockerfile::class.java) {
      group = "docker"
      destFile.set(project.file("${project.buildDir}/Dockerfile"))

      from(dockerImage)

      exposePort(port)
      environmentVariable("JAVA_OPTS_PLUGIN_DEFAULTS", JAVA_OPTS_PLUGIN_DEFAULTS)

      val jarName = "${project.rootProject.name}-${project.rootProject.version}.jar"
      copyFile("libs/$jarName", "/app.jar")

      entryPoint(
        "sh",
        "-c",
        "java \${JAVA_OPTS_GLOBAL_DEFAULTS:-\$JAVA_OPTS_PLUGIN_DEFAULTS} \$JAVA_OPTS_ADDITIONAL -jar /app.jar",
      )
    }
  }

  private fun buildImageTask(project: Project) {
    project.tasks.register("buildImage", DockerBuildImage::class.java) {
      dependsOn("createDockerfile")
      group = "docker"

      inputDir.set(project.buildDir)
      images.add(buildImageNameTag(project))
    }
  }

  private fun pushImageTask(project: Project) {
    project.tasks.register("pushImage", DockerPushImage::class.java) {
      group = "docker"
      images.add(buildImageNameTag(project))
    }
  }

  private fun removeImageTask(project: Project) {
    project.tasks.register("removeImage", DockerRemoveImage::class.java) {
      group = "docker"
      targetImageId(buildImageNameTag(project))
      force.set(true)
      onError { System.out.println("No previous versioned image to delete") }
    }
  }

  private fun addDockerIgnore() {
    val dockerIgnoreFile = File(".dockerignore")
    if (dockerIgnoreFile.exists()) return

    val dockerIgnoreContent = this::class.java.classLoader.getResourceAsStream(".dockerignore")
      ?.bufferedReader()
      ?.readText()
      ?: ""

    dockerIgnoreFile.writeText(dockerIgnoreContent, Charsets.UTF_8)
  }
}
