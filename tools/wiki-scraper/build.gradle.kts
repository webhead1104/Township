import net.kyori.indra.IndraExtension

plugins {
    id("java")
    id("io.freefair.lombok") version "9.1.0"
    id("com.gradleup.shadow") version "9.3.0"
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
    implementation("info.picocli:picocli:4.7.7")
    implementation("org.spongepowered:configurate-gson:4.2.0-GeyserMC-SNAPSHOT")
    implementation("org.apache.commons:commons-text:1.15.0")
    annotationProcessor("info.picocli:picocli-codegen:4.7.7")
}

extensions.configure<IndraExtension> {
    javaVersions {
        target(25)
        minimumToolchain(25)
        testWith(25)
    }
}

application {
    mainClass.set("me.webhead1104.tools.wikiScraper.cli.Main")
}

tasks.getByName<JavaExec>("run") {
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
