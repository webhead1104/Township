plugins {
    id("java")
    id("com.gradleup.shadow") version "9.3.1"
    id("net.cytonic.run-cytosis") version "1.0"
}

version = project.findProperty("plugin_version") as String? ?: "unknown"

repositories {
    maven("https://jitpack.io")
    maven("https://repo.foxikle.dev/cytonic")
}

dependencies {
    compileOnly(project(":platforms:common"))
    implementation(project(":platforms:common")) {
        isTransitive = false
    }
    implementation(libs.zapper)

    compileOnly(variantOf(libs.cytosis) {
        classifier("all")
    }) {
        exclude("me.devnatan")

    }

    compileOnly(libs.lamp.minestom)
    annotationProcessor(libs.autoService.processor)

    implementation(libs.configurate.gson)
    implementation(libs.configurate.yaml)
    implementation(libs.inventoryFramework.api)
    implementation(libs.inventoryFramework.platform)
    implementation(libs.commonsText)

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

val generateClassloader = tasks.register("generateClassloader") {
    val outputDir: File = file("$projectDir/build/generated/sources/classloader")
    val packageDir = File(outputDir, "me/webhead1104/towncraft")
    val classloaderFile = File(packageDir, "Main.java")

    val deps = provider {
        val commonDeps = project(":platforms:common")
            .configurations.getByName("api")
            .dependencies
            .filterIsInstance<ModuleDependency>()
            .filter { it.group != "org.spongepowered" }

        val platformDeps = configurations.getByName("compileOnly")
            .dependencies
            .filterIsInstance<ModuleDependency>()
            .filter { it.group != "net.cytonic" && it.group != "me.webhead1104" }

        (commonDeps + platformDeps).map { "${it.group}:${it.name}:${it.version}" }
    }

    inputs.property("dependencies", deps)
    outputs.file(classloaderFile)

    doLast {
        packageDir.mkdirs()
        outputDir.mkdirs()
        classloaderFile.createNewFile()

        val depsString: String = deps.get().joinToString("\n") {
            """
                dependencyManager.dependency("${it.replace(".", "$")}".replace("$", "."));
            """
        }.trimIndent()

        classloaderFile.writeText(
            """
package me.webhead1104.towncraft;

import net.cytonic.cytosis.plugins.CytosisPlugin;
import revxrsal.zapper.DependencyManager;
import revxrsal.zapper.classloader.URLClassLoaderWrapper;
import revxrsal.zapper.repository.Repository;

import java.io.File;
import java.net.URLClassLoader;

public class Main implements CytosisPlugin {
    static {
        File libraries = new File(
                Towncraft.getDataFolder(),
                "libraries"
        );
        DependencyManager dependencyManager = new DependencyManager(
                libraries,
                URLClassLoaderWrapper.wrap((URLClassLoader) Main.class.getClassLoader())
        );

        dependencyManager.repository(Repository.mavenCentral());
        dependencyManager.repository(Repository.maven("https://jitpack.io"));
        dependencyManager.repository(Repository.maven("https://repo.opencollab.dev/maven-snapshots"));

        $depsString

        dependencyManager.load();
    }

    @Override
    public void initialize() {
        TowncraftCytosis.initialize();
    }

    @Override
    public void shutdown() {
        TowncraftCytosis.shutdown();
    }
}
            """.trimIndent()
        )

        println("Generated Main.java at $classloaderFile")
    }
}

tasks.compileJava {
    dependsOn(generateClassloader)
    source(generateClassloader.map { layout.buildDirectory.dir("generated/sources/classloader").get() })
}

tasks {
    runCytosis {
        cytosisVersion("1.0-SNAPSHOT")
        runDirectory.set(rootProject.layout.projectDirectory.dir("run/cytosis"))
        jvmArgs = listOf("-XX:+AllowEnhancedClassRedefinition")
    }
    assemble {
        dependsOn(shadowJar)
    }
    jar {
        enabled = false
    }
    shadowJar {
        archiveFileName.set("Towncraft-Cytosis-${project.version}.jar")
        archiveClassifier.set("")
        mergeServiceFiles()

//        // Exclude all api dependencies except configurate and IF
        val commonApiDeps = project(":platforms:common")
            .configurations.getByName("api")
            .dependencies
            .filterIsInstance<ModuleDependency>()

        dependencies {
            commonApiDeps
                .filter { it.group != "net.cytonic" && it.group != "org.spongepowered" && it.group != "me.devnatan" }
                .forEach { dep -> exclude(dependency("${dep.group}:${dep.name}:.*")) }
        }

        relocate("revxrsal.zapper", "me.webhead1104.towncraft.libs.zapper")

        relocate("org.spongepowered.configurate", "me.webhead1104.towncraft.libs.configurate")
        relocate("io.leangen.geantyref", "me.webhead1104.towncraft.libs.configurate")
        relocate("com.google.gson", "me.webhead1104.towncraft.libs.configurate")
        relocate("me.devnatan.inventoryframework", "me.webhead1104.towncraft.libs.inventoryframework")
        relocate("org.apache.commons.commons-text", "me.webhead1104.towncraft.libs.apache")
    }
}