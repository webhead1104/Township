plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.2"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("io.freefair.lombok") version "9.1.0"
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.1"
    id("com.diffplug.spotless") version "8.0.0"
}

group = "me.webhead1104"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://eldonexus.de/repository/maven-public/")
    maven("https://repo.tcoded.com/releases")
    maven("https://repo.opencollab.dev/maven-snapshots")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    implementation("org.spongepowered:configurate-yaml:4.2.0-GeyserMC-SNAPSHOT")
    implementation("org.spongepowered:configurate-gson:4.2.0-GeyserMC-SNAPSHOT")
    implementation("me.devnatan:inventory-framework-platform-paper:3.5.5")
    implementation("me.devnatan:inventory-framework-platform-bukkit:3.5.5")
    implementation("io.github.classgraph:classgraph:4.8.184")
    compileOnly("org.mongodb:mongodb-driver-sync:5.7.0-SNAPSHOT")
    compileOnly("com.zaxxer:HikariCP:6.3.3")

    compileOnly("net.strokkur:commands-annotations:1.5.0")
    annotationProcessor("net.strokkur:commands-processor:1.5.0")

    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.98.0")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.1")
    testImplementation("org.spongepowered:configurate-gson:4.2.0")
    testImplementation("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<Javadoc> {
    val javadocOptions = options as CoreJavadocOptions

    javadocOptions.addStringOption("source", "21")
}

val generateClassloader = tasks.register("generateClassloader") {
    val outputDir: File = file("$projectDir/build/generated/sources/classloader")
    val packageDir = File(outputDir, "me/webhead1104/township/utils")
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

        relocate("org.spongepowered.configurate", "me.webhead1104.township.libs.configurate")
        relocate("org.yaml.snakeyaml", "me.webhead1104.township.libs.snakeyaml")
        relocate("io.leangen.geantyref", "me.webhead1104.township.libs.geantyref")
        relocate("com.google.gson", "me.webhead1104.township.libs.gson")

        relocate("me.devnatan.inventoryframework", "me.webhead1104.township.libs.inventoryframework")
        relocate("com.tcoded.folialib", "me.webhead1104.township.libs.folialib")
        relocate("io.github.classgraph", "me.webhead1104.township.libs.classgraph")
        relocate("nonapi.io.github.classgraph", "me.webhead1104.township.libs.classgraph")
    }
    runServer {
        minecraftVersion("1.21.10")

        jvmArgs("-Dlog4j2.configurationFile=log4j2.xml")
    }
    paperPluginYaml {
        main.set("me.webhead1104.township.Township")
        apiVersion.set("1.21")
        author.set("Webhead1104")
        description.set("A remake of the game Township by Playrix in Minecraft")
        loader.set("me.webhead1104.township.utils.GeneratedClassloader")
        website.set("https://github.com/Webhead1104/Township")
    }

    spotless {
        ratchetFrom = "origin/master"
        java {
            idea().withDefaults(true)
            formatAnnotations()
        }
    }
}