import net.kyori.indra.IndraExtension

plugins {
    id("java")
    id("io.freefair.lombok") version "9.1.0"
    id("net.kyori.indra") version "4.0.0" apply false
    id("net.kyori.indra.git") version "4.0.0" apply false
    id("net.kyori.indra.licenser.spotless") version "4.0.0" apply false
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "jacoco")
    apply(plugin = "net.kyori.indra")
    apply(plugin = "net.kyori.indra.git")
    apply(plugin = "net.kyori.indra.licenser.spotless")

    group = "me.webhead1104"
    version = "0.1"

    repositories {
        mavenCentral()
        maven("https://repo.opencollab.dev/maven-snapshots")
        maven("https://jitpack.io/")
    }

    extensions.configure<IndraExtension> {
        javaVersions {
            target(21)
            minimumToolchain(21)
            testWith(21)
        }

        github("Webhead1104", "Towncraft") {
            ci(true)
        }

        mitLicense()
    }

    tasks.withType<JavaCompile> {
        // Preserve parameter names in the bytecode
        options.compilerArgs.add("-parameters")
    }

    tasks.withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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