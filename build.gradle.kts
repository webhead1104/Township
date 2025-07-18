plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-rc1"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.freefair.lombok") version "8.14"
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.0"
}

group = "me.webhead1104"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.7-R0.1-SNAPSHOT")
    compileOnly("org.spongepowered:configurate-gson:4.2.0")

    compileOnly("net.strokkur:strokk-commands-annotations:1.2.4-SNAPSHOT")
    annotationProcessor("net.strokkur:strokk-commands-processor:1.2.4-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<Javadoc> {
    val javadocOptions = options as CoreJavadocOptions

    javadocOptions.addStringOption("source", "21")
}

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
            "version" to project.version,
            "apiVersion" to "1.21"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
    shadowJar {
        archiveFileName.set("Township-${project.version}.jar")
        archiveClassifier.set("")
        mergeServiceFiles()
    }
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.7")
    }
    paperPluginYaml {
        // Defaults for name, version, and description are inherited from the Gradle project
        main.set("me.webhead1104.township.Township")
        apiVersion.set("1.21")
        author.set("Webhead1104")
        description.set("A remake of the game Township by Playrix in Minecraft")
        loader.set("me.webhead1104.township.TownshipLoader")
        website.set("https://github.com/Webhead1104/Township")
    }
}