plugins {
    id("java")
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("com.gradleup.shadow") version "9.3.1"
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
}
version = project.findProperty("plugin_version") as String? ?: "unknown"
val minecraftVersion = findProperty("minecraft_version") as String

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":platforms:common"))
    implementation(project(":platforms:common")) {
        isTransitive = false
        exclude("com.google")
        exclude("org.apache")
        exclude("org.intellij")
        exclude("org.jetbrains")
        exclude("org.slf4j")
        exclude("org.jspecify")
        exclude("net.kyori")
    }

    compileOnly(libs.lamp.bukkit)
    compileOnly("io.papermc.paper:paper-api:$minecraftVersion-R0.1-SNAPSHOT")
    implementation(libs.bstats)
    annotationProcessor(libs.autoService.processor)

    //shadow these in since paper class loading is weird
    implementation(libs.configurate.gson)
    implementation(libs.configurate.yaml)
    implementation(libs.inventoryFramework.api)
    implementation(libs.inventoryFramework.platform)

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

val generateClassloader: TaskProvider<Task> = tasks.register("generateClassloader") {
    val outputDir: File = file("$projectDir/build/generated/sources/classloader")
    val packageDir = File(outputDir, "me/webhead1104/towncraft/utils")
    val classloaderFile = File(packageDir, "GeneratedClassloader.java")

    val deps = provider {
        val commonDeps = project(":platforms:common")
            .configurations.getByName("api")
            .dependencies
            .filterIsInstance<ModuleDependency>()
            .filter { it.group != "org.spongepowered" }

        val platformDeps = configurations.getByName("compileOnly")
            .dependencies
            .filterIsInstance<ModuleDependency>()
            .filter { it.group != "io.papermc.paper" && it.group != "me.webhead1104" }

        (commonDeps + platformDeps).map { "${it.group}:${it.name}:${it.version}" }
    }

    inputs.property("dependencies", deps)
    outputs.file(classloaderFile)

    doLast {
        packageDir.mkdirs()
        outputDir.mkdirs()
        classloaderFile.createNewFile()

        val depsString: String = deps.get().joinToString("\n                    ") {
            "resolver.addDependency(new Dependency(new DefaultArtifact(\"$it\"), null));"
        }

        classloaderFile.writeText(
            """
            package me.webhead1104.towncraft.utils;
            
            import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
            import io.papermc.paper.plugin.loader.PluginLoader;
            import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
            import org.eclipse.aether.artifact.DefaultArtifact;
            import org.eclipse.aether.graph.Dependency;
            import org.eclipse.aether.repository.RemoteRepository;
            import org.jetbrains.annotations.NotNull;

            public class GeneratedClassloader implements PluginLoader {

                @Override
                public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
                    System.setProperty("bstats.relocatecheck", "false");
                    MavenLibraryResolver resolver = new MavenLibraryResolver();
                    resolver.addRepository(new RemoteRepository.Builder("geyser", "default", "https://repo.opencollab.dev/maven-snapshots").build());
                    resolver.addRepository(new RemoteRepository.Builder("central", "default", MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR).build());
                    
                    $depsString

                    classpathBuilder.addLibrary(resolver);
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

tasks {
    jar {
        enabled = false
    }
    runServer {
        minecraftVersion(minecraftVersion)
        runDirectory.set(rootProject.layout.projectDirectory.dir("run/paper"))
    }
    paperPluginYaml {
        name.set("Towncraft")
        main.set("me.webhead1104.towncraft.TowncraftPaper")
        apiVersion.set("1.21")
        author.set("Webhead1104")
        description.set("A remake of the game Township by Playrix in Minecraft")
        loader.set("me.webhead1104.towncraft.utils.GeneratedClassloader")
        website.set("https://github.com/Webhead1104/Towncraft")
    }

    shadowJar {
        archiveFileName.set("Towncraft-Paper-${project.version}.jar")
        archiveClassifier.set("")
        mergeServiceFiles()

        // Exclude all api dependencies except configurate and IF
        val commonApiDeps = project(":platforms:common")
            .configurations.getByName("api")
            .dependencies
            .filterIsInstance<ModuleDependency>()

        dependencies {
            commonApiDeps
                .filter { it.group != "org.spongepowered" && it.group != "me.devnatan" }
                .forEach { dep ->
                    exclude(dependency("${dep.group}:${dep.name}:.*"))
                }
        }

        relocate("org.spongepowered.configurate", "me.webhead1104.towncraft.libs.configurate")
        relocate("io.leangen.geantyref", "me.webhead1104.towncraft.libs.configurate")
        relocate("com.google.gson", "me.webhead1104.towncraft.libs.configurate")
        relocate("org.bstats", "me.webhead1104.towncraft.libs.bstats")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}