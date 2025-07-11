import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java")
    application
    checkstyle
    jacoco
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("se.patrikerdes.use-latest-versions") version "0.2.18"
    id("com.github.ben-manes.versions") version "0.52.0"
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
    implementation("io.javalin:javalin:6.7.0")
    implementation("io.javalin:javalin-rendering:6.7.0")
    implementation("gg.jte:jte:3.2.1")
    implementation("org.slf4j:slf4j-simple:2.1.0-alpha1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.1")

    // LOMBOK
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
    // CheckStyle
    implementation("com.puppycrawl.tools:checkstyle:10.26.1")
    // Special for PostgreSQL
    implementation("org.postgresql:postgresql:42.7.7")
    // database H2 & HikariCP
    implementation("com.h2database:h2:2.3.232")
    implementation("com.zaxxer:HikariCP:6.3.0")

    // Unirest Java
    implementation("com.konghq:unirest-java:3.14.5")

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.0-M1")
    testImplementation(platform("org.junit:junit-bom:6.0.0-M1"))
    testImplementation("io.javalin:javalin-testtools:6.7.0")
    testImplementation("org.assertj:assertj-core:4.0.0-M1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.0-M1")

    // MockWebServer » 5.1.0
//    testImplementation("com.squareup.okhttp3:mockwebserver:5.1.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}

checkstyle {
    toolVersion = "10.26.1"
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
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        // showStackTraces = true
        // showCauses = true
        showStandardStreams = true
    }

}

sonar {
    properties {
        property("sonar.projectKey", "VictorGotsenko_java-project-72")
        property("sonar.organization", "victorgotsenko")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
