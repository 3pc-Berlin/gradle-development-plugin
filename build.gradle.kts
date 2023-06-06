@file:Suppress("UnstableApiUsage")

plugins {
	`java-gradle-plugin`
	`kotlin-dsl`
	kotlin("jvm") version "1.8.0"
	id("com.gradle.plugin-publish") version "1.2.0"
	id("de.3pc.development") version "0.0.6-dev.54"
}

group = "de.3pc"

gradlePlugin {
	plugins {
		create("dreipcDevPlugin") {
			website.set("https://3pc.de")
			vcsUrl.set("https://github.com/3pc-Berlin/gradle-development-plugin")
			id = "de.3pc.development"
			displayName = "3pc Java Development Plugin"
			description = "Pre configured external plugins for 3pc Java Projects, helps to reduce boiler plate configs and push standards."
			implementationClass = "dreipc.plugins.development.DevelopmentPlugin"
			tags.set(listOf("dev", "java", "lombok", "lint", "docker", "versioning", "test"))
		}
	}
}

repositories {
	maven(url = "https://nexus.3pc.de/repository/maven-group/")
	maven(url = "https://plugins.gradle.org/m2/")
	mavenLocal()
}

dependencies {
	// Kotlin
	implementation(kotlin("stdlib", "1.8.0"))

	//Plugins see: https://plugins.gradle.org/
	implementation("io.freefair.lombok:io.freefair.lombok.gradle.plugin:8.0.1")
	implementation("io.wusa.semver-git-plugin:io.wusa.semver-git-plugin.gradle.plugin:2.3.7")

	implementation("com.diffplug.spotless:spotless-plugin-gradle:6.19.0")
	implementation("net.ltgt.gradle:gradle-errorprone-plugin:3.1.0")
	implementation("net.ltgt.gradle:gradle-nullaway-plugin:1.6.0")

	implementation("com.bmuschko:gradle-docker-plugin:9.3.1")

	// Testing
	testImplementation("org.assertj:assertj-core:3.24.2")
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")

	testImplementation("org.springframework.boot:spring-boot-gradle-plugin:3.1.0")
}

extensions.findByName("buildScan")?.withGroovyBuilder {
	setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
	setProperty("termsOfServiceAgree", "yes")
}

publishing {
	repositories {
		mavenLocal()
	}
}

