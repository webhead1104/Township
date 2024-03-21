plugins {
    `java-library`
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.5.11"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenLocal()
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://jitpack.io/")
    maven("https://repo.rapture.pw/repository/maven-releases/")
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    api("net.wesjd:anvilgui:1.9.2-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("com.github.foxikle:woodymenus:main-SNAPSHOT")
}

group = "me.webhead1104"
version = "testing"
description = "Township"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks {
    assemble {
        dependsOn(reobfJar)
        dependsOn(shadowJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(17)
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

    reobfJar {
        outputJar.set(layout.buildDirectory.file(providers.gradleProperty("plugin_dir").get() + "/Township-${project.version}.jar"))
    }
}