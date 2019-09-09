import io.franzbecker.gradle.lombok.task.DelombokTask
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    application
    eclipse
    idea
    id("com.diffplug.gradle.spotless") version "3.24.1"
    id("com.github.ben-manes.versions") version "0.22.0"
    id("io.franzbecker.gradle-lombok") version "3.1.0"
}

repositories {
    jcenter()
}

dependencies {
    compileOnly("org.projectlombok:lombok")

    implementation("org.slf4j:slf4j-api:1.7.28")
    implementation("org.apache.logging.log4j:log4j-api:2.12.1")
    implementation("org.apache.logging.log4j:log4j-core:2.12.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.12.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.9.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.9")

	testImplementation("org.junit.jupiter:junit-jupiter:5.5.1")
}

application {
    mainClassName = "de.claudioaltamura.kafka.topic.suggest.KafkaReassignPartitionsSuggest"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

spotless {
    java {
        googleJavaFormat()
    }
}

tasks {
    val delombok by registering(DelombokTask::class)

    delombok {
        dependsOn(compileJava)
        val outputDir by extra { file("$buildDir/delombok") }
        outputs.dir(outputDir)
        sourceSets.getByName("main").java.srcDirs.forEach {
            inputs.dir(it)
            args(it, "-d", outputDir)
        }
        doFirst {
            outputDir.delete()
        }
    }

    javadoc {
        dependsOn(delombok)
        val outputDir: File by delombok.get().extra
        source = fileTree(outputDir)
        isFailOnError = false

        if (JavaVersion.current().isJava11Compatible) {
            (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
        }
    }

    test {
        useJUnitPlatform()
        testLogging {
            events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
 }
