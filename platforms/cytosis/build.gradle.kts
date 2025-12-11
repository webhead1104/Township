plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.2"
}

repositories {
    maven("https://jitpack.io")
    mavenLocal()
    maven("https://repo.foxikle.dev/cytonic")
}

dependencies {
    compileOnly(project(":platforms:common"))
    implementation(project(":platforms:common")) {
        isTransitive = false
    }
    implementation("io.github.revxrsal:zapper.api:1.0.3")

    compileOnly("com.github.Takasaki-Studio-Games:MinestomPvP:751353385c")
    compileOnly("net.cytonic:Cytosis:1.0-SNAPSHOT") {
        exclude("me.devnatan")
        exclude("com.github.TogAr2", "MinestomPvP")
    }
    compileOnly("io.github.revxrsal:lamp.minestom:4.0.0-rc.14")


    implementation("org.spongepowered:configurate-gson:4.2.0-GeyserMC-SNAPSHOT")
    implementation("org.spongepowered:configurate-yaml:4.2.0-GeyserMC-SNAPSHOT")
    implementation("me.devnatan:inventory-framework-api:3.5.5")
    implementation("me.devnatan:inventory-framework-platform:3.5.5")

    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.1")
}
tasks.test {
    useJUnitPlatform()
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

        val depsString: String = deps.get().joinToString("\n                    ") {
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

        println("Generated GeneratedClassloader.java at $classloaderFile")
    }
}

tasks.compileJava {
    dependsOn(generateClassloader)
    source(generateClassloader.map { layout.buildDirectory.dir("generated/sources/classloader").get() })
}

val serverDirProvider = providers.gradleProperty("server_dir")

tasks {
    if (serverDirProvider.isPresent) {
        register<Copy>("copyJarToPluginFolder") {
            from(shadowJar)
            into(file(serverDirProvider.get()).resolve("plugins"))
        }
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
        if (serverDirProvider.isPresent) {
            finalizedBy("copyJarToPluginFolder")
        }

//        // Exclude all api dependencies except configurate and IF
        val commonApiDeps = project(":platforms:common")
            .configurations.getByName("api")
            .dependencies
            .filterIsInstance<ModuleDependency>()

        dependencies {

            commonApiDeps.forEach { IO.println("HEY!!!!!!!! $it") }
            commonApiDeps
                .filter { it.group != "net.cytonic" && it.group != "org.spongepowered" && it.group != "me.devnatan" }
                .forEach { dep ->
                    IO.println("excluding $dep")
                    exclude(dependency("${dep.group}:${dep.name}:.*"))
                }
        }

        relocate("revxrsal.zapper", "me.webhead1104.towncraft.libs.zapper")

        relocate("org.spongepowered.configurate", "me.webhead1104.towncraft.libs.configurate")
        relocate("io.leangen.geantyref", "me.webhead1104.towncraft.libs.configurate")
        relocate("com.google.gson", "me.webhead1104.towncraft.libs.configurate")
        relocate("me.devnatan.inventoryframework", "me.webhead1104.towncraft.libs.inventoryframework")
    }
}