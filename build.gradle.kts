plugins {
	`java-gradle-plugin`
	`kotlin-dsl`
	kotlin("jvm") version "1.8.10"
	id("com.gradle.plugin-publish") version "1.1.0"
	id("dreipc.development") version "0.0.1"
}

group = "dreipc"
version = "0.0.1"
java.setSourceCompatibility(17)

gradlePlugin {
	plugins {
		create("dreipcDevPlugin") {
			id = "dreipc.development"
			displayName = "3pc Java Development Plugin"
			description = "Pre configured external plugins for 3pc Java Projects."
			implementationClass = "dreipc.plugins.development.DevelopmentPlugin"
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
	implementation(kotlin("stdlib", "1.8.10"))

	//Plugins
	implementation("io.freefair.lombok:io.freefair.lombok.gradle.plugin:6.6.3")
	implementation("io.wusa.semver-git-plugin:io.wusa.semver-git-plugin.gradle.plugin:2.3.7")

	implementation("com.diffplug.spotless:spotless-plugin-gradle:6.17.0")
	implementation("net.ltgt.gradle:gradle-errorprone-plugin:3.0.1")
	implementation("net.ltgt.gradle:gradle-nullaway-plugin:1.5.0")

	// Testing
	testImplementation("org.assertj:assertj-core:3.24.2")
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
}

tasks.withType<Test>{
	useJUnitPlatform()
}

publishing {
	repositories {
		mavenLocal()
	}
}

