plugins {
    id("java")
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("com.gradleup.shadow") version "9.2.2"
}

group = "me.webhead1104"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":platforms:common")) {
        exclude("com.google")
        exclude("org.apache")
        exclude("org.intellij")
        exclude("org.jetbrains")
        exclude("org.jspecify")
    }
    implementation("studio.mevera:imperat-bukkit:2.2.0")
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
}

val generateClassloader = tasks.register("generateClassloader") {
    val outputDir: File = file("$projectDir/build/generated/sources/classloader")
    val packageDir = File(outputDir, "me/webhead1104/towncraft/utils")
    val classloaderFile = File(packageDir, "GeneratedClassloader.java")

    val deps = provider {
        configurations.getByName("compileOnly")
            .dependencies
            .filterIsInstance<ModuleDependency>()
            .filter { it.group != "io.papermc.paper" && it.group != "net.strokkur" }
            .map { "${it.group}:${it.name}:${it.version}" }
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
        minecraftVersion("1.21.10")

        jvmArgs("-Dlog4j2.configurationFile=log4j2.xml")
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

        from(project(":platforms:common").sourceSets["main"].output)

        relocate("org.spongepowered.configurate", "me.webhead1104.towncraft.libs.configurate")
        relocate("io.leangen.geantyref", "me.webhead1104.towncraft.libs.geantyref")
        relocate("io.github.classgraph", "me.webhead1104.towncraft.libs.classgraph")
        relocate("nonapi.io.github.classgraph", "me.webhead1104.towncraft.libs.classgraph")
        relocate("me.devnatan.inventoryframework", "me.webhead1104.towncraft.libs.inventoryframework")

//
//        relocate("org.yaml.snakeyaml", "me.webhead1104.towncraft.libs.snakeyaml")
//        relocate("com.google", "me.webhead1104.towncraft.libs.google")
//        relocate("com.tcoded.folialib", "me.webhead1104.towncraft.libs.folialib")
    }
}
