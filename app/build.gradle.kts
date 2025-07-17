import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent


plugins {
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
// const val mockitoVersion = "4.5.1"

val junitVer = "6.0.0-M1"
val javalinVer = "6.7.0"
val lombokVer = "1.18.38"
val checkstyleVer = "10.26.1"
val jteVer = "3.2.1"
val slf4jVer = "2.1.0-alpha1"
val jsoupVer = "1.21.1"
val postgresqlVer = "42.7.7"
val unirestVer = "3.14.5"
val jacksonVer = "2.19.1"
val h2databaseVer = "2.3.232"
val HikariCPVer = "6.3.0"
val mockwebserverVer = "4.12.0"

dependencies {
    // Javalin
    implementation("io.javalin:javalin:$javalinVer")
    implementation("io.javalin:javalin-rendering:$javalinVer")
    implementation("gg.jte:jte:$jteVer")
    implementation("org.slf4j:slf4j-simple:$slf4jVer")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVer")
    implementation("com.puppycrawl.tools:checkstyle:$checkstyleVer") /* CheckStyle */
    implementation("org.postgresql:postgresql:$postgresqlVer") // Special for PostgreSQL
    implementation("com.h2database:h2:$h2databaseVer") // database H2 & HikariCP
    implementation("com.zaxxer:HikariCP:$HikariCPVer")
    implementation("com.konghq:unirest-java:$unirestVer") // Unirest Java
    implementation("org.jsoup:jsoup:$jsoupVer") // jsoup HTML parser library @ https://jsoup.org/

    // LOMBOK
    compileOnly("org.projectlombok:lombok:$lombokVer")
    annotationProcessor("org.projectlombok:lombok:$lombokVer")
    testCompileOnly("org.projectlombok:lombok:$lombokVer")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVer")
    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVer")
    testImplementation(platform("org.junit:junit-bom:$junitVer"))
    testImplementation("io.javalin:javalin-testtools:$javalinVer")
    testImplementation("org.assertj:assertj-core:4.0.0-M1")
    testImplementation("com.squareup.okhttp3:mockwebserver:$mockwebserverVer") // MockWebServer » 5.1.0

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:$junitVer")
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
