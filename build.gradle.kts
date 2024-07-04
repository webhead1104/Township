import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.webhead1104"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.foxikle.dev/cytonic")
}

dependencies {
    compileOnly("net.cytonic:Cytosis:1.0-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.34") // lombok
    annotationProcessor("org.projectlombok:lombok:1.18.34") // lombok
}

tasks.withType<JavaCompile> {
    // use String templates
    options.compilerArgs.add("--enable-preview")
}

tasks {
    assemble {
        dependsOn("shadowJar")
    }
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes["Main-Class"] = "me.webhead1104.township.Township"
        }
        mergeServiceFiles()
        archiveFileName.set("Township-${project.version}.jar")
        archiveClassifier.set("")
        destinationDirectory.set(File(providers.gradleProperty("server_dir").get() + "/plugins"))
    }
}