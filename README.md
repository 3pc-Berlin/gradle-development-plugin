# Gradle Development Plugin 
Author: Sören Räuchle @ [3pc GmbH](https://3pc.de)

Gradle Plugin for Java Projects. This Plugin reduces boilerplate Code of java development Projects and sets up some standard tasks for testing and code-quality.

- Unit-, Integration and E2E testing tasks
- Error-prone and nullify Code Checks
- Code style checks including automated formatting (Plantir style)
- Git Commit message verify and check
- Semantic Versioning
- Lombok Code generation
- Dockerization of java code (tbd)

3pc Development Gradle Plugin for usage in all Java Projects
 
More information is available on the gradle [website](https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_markers)

---

### Install the Plugin
Add the plugin inside your `build.gradle(.kts)`
```groovy
// build.gradle(.kts)
plugins{
  id("dreipc.development") version "0.0.1"
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