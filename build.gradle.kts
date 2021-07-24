import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import com.palantir.gradle.docker.*

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("com.palantir.docker") version "0.26.0"
    id("com.palantir.docker-compose") version "0.26.0"
}

group = "edu.nau.cs386"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val vertxVersion = "4.1.1"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "edu.nau.cs386.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
    mainClass.set(launcherClassName)
}

dependencies {
    implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
    implementation("io.vertx:vertx-web-templ-handlebars")
    implementation("io.vertx:vertx-sql-client-templates")
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-pg-client")
    implementation("io.vertx:vertx-mail-client")
    implementation("io.vertx:vertx-json-schema")
    implementation("io.vertx:vertx-auth-sql-client")
    implementation("io.vertx:vertx-auth-jdbc")
    implementation("io.vertx:vertx-config:4.1.1")
    testImplementation("io.vertx:vertx-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    implementation("com.thedeanda:lorem:2.1")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("commons-io:commons-io:2.11.0")

}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    withType<ShadowJar> {
        archiveClassifier.set("fat")
        manifest {
            attributes(mapOf("Main-Verticle" to mainVerticleName))
        }
        mergeServiceFiles()
    }

    withType<ShadowJar> {
        archiveClassifier.set("fat")
        manifest {
            attributes(mapOf("Main-Verticle" to mainVerticleName))
        }
        mergeServiceFiles()
    }

    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events = setOf(PASSED, SKIPPED, FAILED)
        }
    }

    withType<JavaExec> {
        args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")

    }

    task<Exec>("buildDocker") {
        dependsOn("shadowJar")
        commandLine("docker", "build", "-t", "vertx-app", ".")

    }

    withType<DockerComposeUp> {
        dependsOn("buildDocker")
    }

    task<Exec>("execDockerComposeDown") {
        dependsOn("buildDocker")
        commandLine("docker-compose", "down")
    }

    task<Exec>("execDockerComposeUp") {
        dependsOn("execDockerComposeDown")
        commandLine("docker-compose", "up", "--build")
    }

    create("stage") {
        dependsOn("shadowJar")
    }

}

