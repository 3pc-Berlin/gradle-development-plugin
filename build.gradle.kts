plugins {
	`java-gradle-plugin`
	`kotlin-dsl`
	java
	kotlin("jvm") version "1.8.10"
	id("com.gradle.plugin-publish") version "1.1.0"
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
	mavenLocal()
}

dependencies {
	// Kotlin
	implementation(kotlin("stdlib", "1.8.10"))

	//Plugins
	// https://mvnrepository.com/artifact/io.freefair.lombok/io.freefair.lombok.gradle.plugin
	implementation("io.freefair.lombok:io.freefair.lombok.gradle.plugin:6.6.3")


	// Testing
	testImplementation("org.assertj:assertj-core:3.24.2")
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

publishing {
	repositories {
		mavenLocal()
	}
}

