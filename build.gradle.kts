plugins {
    id("java")
    id("io.freefair.lombok") version "9.1.0"
    id("com.diffplug.spotless") version "8.1.0"
}

allprojects {
    apply(plugin = "io.freefair.lombok")
    repositories {
        mavenCentral()
        maven("https://repo.opencollab.dev/maven-snapshots")
        maven("https://jitpack.io/")
    }

    tasks.withType<JavaCompile> {
        // Preserve parameter names in the bytecode
        options.compilerArgs.add("-parameters")
    }
    group = "me.webhead1104"
    version = "1.0-SNAPSHOT"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<Javadoc> {
    val javadocOptions = options as CoreJavadocOptions

    javadocOptions.addStringOption("source", "21")
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