plugins {
    id("java")
    id("io.freefair.lombok") version "9.1.0"
    id("com.diffplug.spotless") version "8.1.0"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "jacoco")

    group = "me.webhead1104"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven("https://repo.opencollab.dev/maven-snapshots")
        maven("https://jitpack.io/")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    tasks.withType<JavaCompile> {
        // Preserve parameter names in the bytecode
        options.compilerArgs.add("-parameters")
    }

    plugins.withType<JavaPlugin> {
        tasks.withType<Test> {
            finalizedBy(tasks.named("jacocoTestReport"))

            useJUnitPlatform()
            maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
            reports {
                junitXml.required.set(true)
                html.required.set(true)
            }
        }

        tasks.named<JacocoReport>("jacocoTestReport") {
            dependsOn(tasks.named("test"))

            reports {
                xml.required.set(true)
                html.required.set(true)
                csv.required.set(false)
            }

            // Ensure the report is generated even if there are no tests yet
            classDirectories.setFrom(
                files(classDirectories.files.map {
                    fileTree(it) {
                        exclude(
                            // Exclude generated code, data classes, etc.
                            "**/BuildConfig.*",
                            "**/*$*.*",  // Inner classes
                        )
                    }
                })
            )
        }
    }
}

tasks {
    spotless {
        ratchetFrom = "origin/master"
        java {
            idea().withDefaults(true)
            formatAnnotations()
        }
    }
}