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
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("io.github.classgraph:classgraph:4.8.184")
    implementation("org.slf4j:slf4j-api:2.1.0-alpha1")
    implementation("ch.qos.logback:logback-classic:1.5.21")
    implementation("info.picocli:picocli:4.7.7")
    annotationProcessor("info.picocli:picocli-codegen:4.7.7")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

application {
    mainClass.set("me.webhead1104.tools.wikiScraper.cli.Main")
}

tasks.withType<Javadoc> {
    val javadocOptions = options as CoreJavadocOptions
    javadocOptions.addStringOption("source", "21")
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
