package dreipc.plugins.development.modul

import com.bmuschko.gradle.docker.DockerJavaApplicationPlugin
import com.bmuschko.gradle.docker.tasks.AbstractDockerRemoteApiTask
import com.bmuschko.gradle.docker.tasks.RegistryCredentialsAware
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.internal.os.OperatingSystem.*
import org.gradle.kotlin.dsl.get


class Docker : Plugin<Project> {

    private val DEFAULT_REPO_URL = "nexus.3pc.de"
    private val DOCKER_BASE_IMAGES = mapOf(17 to "nexus.3pc.de/java:17")
    private var DEFAULT_EXPOSE_PORT: Int = 8080
    private var javaOptsDefault: String = """
         -Djava.security.egd=file:/dev/./urandom -XX:TieredStopAtLevel=1
         -Dspring.jmx.enabled=true -Dspring.config.location=classpath:/application.yml
         -Djava.awt.headless=true
         -Dfile.encoding=UTF-8 -DLC_CTYPE=UTF-8
    """.trimIndent()

    override fun apply(project: Project) {
        project.plugins.apply(DockerJavaApplicationPlugin::class.java)

        configureRegistryCredentials(project)
        configureDockerAPI(project)

        createDockerFileTask(project)
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
                this.registryCredentials.url.set(repoUrl)
                this.registryCredentials.username.set(user)
                this.registryCredentials.password.set(password)
                this.registryCredentials.email.set(email)
            }
    }

    private fun configureDockerAPI(
        project: Project,
    ) {
        val daemonUrl = if (current().isWindows()) "tcp://127.0.0.1:2375" else "unix:///var/run/docker.sock"

        project.tasks.withType(AbstractDockerRemoteApiTask::class.java)
            .configureEach {
                this.url.set(daemonUrl)
            }
    }

    private fun createDockerFileTask(project: Project){
        project.tasks.register("createDockerfile", Dockerfile::class.java){
            group = "docker"
            destFile.set(project.file("${project.buildDir}/Dockerfile"))

            val javaPlugin = project.extensions.get("java") as JavaPluginExtension
            val javaVersion = javaPlugin.sourceCompatibility.ordinal

            from(DOCKER_BASE_IMAGES[javaVersion])
            exposePort(DEFAULT_EXPOSE_PORT)
            environmentVariable("JAVA_OPTS_LOCAL_DEFAULTS",javaOptsDefault)
            environmentVariable("JAVA_OPTS_HARDCODED","")

            val jarName = "${project.rootProject.name}-${project.rootProject.version}.jar"
            copyFile("libs/${jarName}", "/app.jar")

            entryPoint("sh", "-c", "java \${JAVA_OPTS_GLOBAL_DEFAULTS_11:-\$JAVA_OPTS_LOCAL_DEFAULTS} \$JAVA_OPTS_SPECIFIC \$JAVA_OPTS_HARDCODED -jar /app.jar")
        }
    }
}