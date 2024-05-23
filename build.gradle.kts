plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io/")
    maven("https://repo.codemc.io/repository/maven-snapshots")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("net.wesjd:anvilgui:1.9.3-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("com.github.foxikle:woodymenus:main-SNAPSHOT")
}

group = "me.webhead1104"
version = "0.1"
description = "Township in minecraft"
java.sourceCompatibility = JavaVersion.VERSION_21

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(21)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
        val props = mapOf(
                "name" to project.name,
                "version" to project.version,
                "description" to project.description,
                "apiVersion" to "1.20"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
    shadowJar {
        archiveFileName = "Township-${project.version}.jar"
        destinationDirectory.set(file(layout.buildDirectory.file(providers.gradleProperty("township_dir").get())))
    }
}
