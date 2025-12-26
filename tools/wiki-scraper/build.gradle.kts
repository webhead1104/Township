plugins {
    id("io.freefair.lombok") version "9.1.0"
    id("com.gradleup.shadow") version "9.3.0"
    kotlin("jvm") version "2.3.0"
    application
}

group = "me.webhead1104"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.21.2")
    implementation("io.github.classgraph:classgraph:4.8.184")
    implementation("org.slf4j:slf4j-api:2.1.0-alpha1")
    implementation("ch.qos.logback:logback-classic:1.5.23")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")
    implementation("com.github.ajalt.clikt:clikt:5.0.3")
    implementation("org.apache.commons:commons-text:1.15.0")
    implementation("org.spongepowered:configurate-gson:4.2.0-GeyserMC-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

application {
    mainClass.set("me.webhead1104.towncraft.wikiScraper.MainKt")
}

tasks.getByName<JavaExec>("run") {
    jvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
    args = listOf(
        "-o",
        rootProject.projectDir.resolve("platforms/common/src/main/resources").absolutePath,
        "-a"
    )
}

tasks {
    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName.set("WikiScraper-${project.version}.jar")
        archiveClassifier.set("")
        mergeServiceFiles()
    }
}
