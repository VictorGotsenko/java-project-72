import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java")
    id("application")
    id("checkstyle")
    id("jacoco")

    id("com.github.johnrengelman.shadow") version "8.1.1"

    id("se.patrikerdes.use-latest-versions") version "0.2.18"
    id("com.github.ben-manes.versions") version "0.51.0"

    id("org.sonarqube") version "6.2.0.5505"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
        mainClass = "hexlet.code.App"
}

repositories {
    mavenCentral()
}

dependencies {
    // Javalin
    implementation("io.javalin:javalin:6.5.0")
    implementation("io.javalin:javalin-rendering:6.5.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("gg.jte:jte:3.1.16")


    // LOMBOK
    compileOnly("org.projectlombok:lombok:1.18.38")
	annotationProcessor("org.projectlombok:lombok:1.18.38")

	testCompileOnly("org.projectlombok:lombok:1.18.38")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.38")



    // CheckStyle
    implementation("com.puppycrawl.tools:checkstyle:10.23.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

checkstyle {
    toolVersion = "10.23.1"
    configFile = file("config/checkstyle/checkstyle.xml")
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
}

tasks.test {
    useJUnitPlatform()
}

sonar {
    properties {
        property("sonar.projectKey", "VictorGotsenko_java-project-72")
        property("sonar.organization", "victorgotsenko")
        property("sonar.host.url", "https://sonarcloud.io")
  }
}
