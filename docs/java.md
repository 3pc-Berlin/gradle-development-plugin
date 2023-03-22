## Java pre-defined settings (03.2023)
- **sourceCompatibility**: **17** (latest Java LTS-Version)
- **repositories**: 3pc Maven (https://nexus.3pc.de/repository/maven-group/) & mavenLocal()
- enable Gradle Java Plugin

This code is not necessary anymore and can be removed:
```groovy
// @build.gradle(.kts)
plugins {
    id "java"
}

repositories {
    maven(url = "https://nexus.3pc.de/repository/maven-group/")
    mavenLocal()
}

sourceCompatibility = 17
```