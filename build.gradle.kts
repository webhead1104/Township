plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-rc3"
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
    maven("https://repo.tcoded.com/releases")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    compileOnly("org.spongepowered:configurate-gson:4.2.0")
    implementation("me.devnatan:inventory-framework-platform-paper:3.5.0")
    implementation("me.devnatan:inventory-framework-platform-bukkit:3.5.0")
    implementation("io.github.classgraph:classgraph:4.8.181")

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

val generateClassloader = tasks.register("generateClassloader") {

    val outputDir = file("$projectDir/build/generated/sources/classloader")
    val packageDir = File(outputDir, "me/webhead1104/webhead1104/township/utils")
    val classloaderFile = File(packageDir, "GeneratedClassloader.java")

    doLast {
        packageDir.mkdirs()
        outputDir.mkdirs()
        classloaderFile.createNewFile()

        val deps = configurations.getByName("compileOnly")
            .dependencies
            .filterIsInstance<ModuleDependency>()
            .filter { it.group != "io.papermc.paper" && it.group != "net.strokkur" }
            .map { "${it.group}:${it.name}:${it.version}" }

        classloaderFile.writeText(
            """
            package me.webhead1104.township.utils;
            
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
                    
                    ${deps.joinToString("\n                    ") { "resolver.addDependency(new Dependency(new DefaultArtifact(\"$it\"), null));" }}

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
        relocate("me.devnatan.inventoryframework", "me.webhead1104.township.libs.inventoryframework")
        relocate("com.tcoded.folialib", "me.webhead1104.township.libs.folialib")
        relocate("io.github.classgraph","me.webhead1104.township.libs.classgraph")
        relocate("nonapi.io.github.classgraph","me.webhead1104.township.libs.classgraph")
    }
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.8")
    }
    paperPluginYaml {
        // Defaults for name, version, and description are inherited from the Gradle project
        main.set("me.webhead1104.township.Township")
        apiVersion.set("1.21")
        author.set("Webhead1104")
        description.set("A remake of the game Township by Playrix in Minecraft")
        loader.set("me.webhead1104.township.utils.GeneratedClassloader")
        website.set("https://github.com/Webhead1104/Township")
    }
}