# Gradle Development Plugin 
Author: Sören Räuchle | [3pc GmbH](https://3pc.de)

Gradle Plugin for Java Projects. This Plugin reduces (build) boilerplate Code of java development Projects and sets up some standard tasks.

#### Features:
- Unit-, Integration and E2E testing tasks
- Error-prone and nullify Code Checks
- Code style checks including automated formatting (Palantir style)
- Git Commit message verify and check
- Semantic Versioning based on git tags
- Lombok Code generation
- Dockerization of java code

Used Plugins: @see `build.gradle.kts`

More Info:
- [testing](/docs/testing.md)
- [code-quality](/docs/code-quality.md)
- [java config](/docs/java.md)
- [spring boot config](/docs/spring.md)
- [docker](/docs/docker.md)

---

### Install the Plugin
Add the plugin inside your `build.gradle(.kts)`
```groovy
// build.gradle(.kts)
plugins{
    id("de.3pc.development") version "0.0.4"
}
```

**Development**: During local development the plugin will be published on `mavenLocal`. Please make sure you added the repository inside your `settings.gradle` to get the plugin dev version. 
```groovy
// settings.gradle
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
rootProject.name = "Your great project name goes here!" 

```

## Environment variables

CI integration variables

- CI_APP_VERSION - allow to enforce a CI-standard versioning scheme
- CI_DOCKER_NAMEONLY - allow to enforce a CI-standard docker naming convention

See Docker runtime variables in [docs/docker.md](docs/docker.md)

