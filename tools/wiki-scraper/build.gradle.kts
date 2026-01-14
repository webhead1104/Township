plugins {
    id("io.freefair.lombok") version "9.2.0"
    id("com.gradleup.shadow") version "9.3.1"
    kotlin("jvm") version "2.3.0"
    application
}

group = "me.webhead1104"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jsoup)
    implementation(libs.classgraph)
    implementation(libs.slf4j)
    implementation(libs.logback)
    implementation(libs.kotlin.logging)
    implementation(libs.clikt)
    implementation(libs.commonsText)
    implementation(libs.configurate.gson)
    implementation(kotlin("reflect"))
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
