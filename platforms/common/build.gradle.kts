plugins {
    id("java")
    id("java-library")
}

group = "me.webhead1104"
version = "1.0-SNAPSHOT"

dependencies {
    api("me.devnatan:inventory-framework-api:3.5.5")
    api("me.devnatan:inventory-framework-platform:3.5.5")
    api("com.google.guava:guava:33.5.0-jre")
    api("org.jetbrains:annotations:26.0.2-1")
    api("org.spongepowered:configurate-gson:4.2.0-GeyserMC-SNAPSHOT")
    api("org.spongepowered:configurate-yaml:4.2.0-GeyserMC-SNAPSHOT")
    api("io.github.classgraph:classgraph:4.8.184")
    api("net.kyori:adventure-text-minimessage:4.25.0")
    api("org.apache.commons:commons-lang3:3.20.0")
    api("org.slf4j:slf4j-api:2.1.0-alpha1")
    api("org.mongodb:mongodb-driver-sync:5.7.0-alpha0")
    api("com.zaxxer:HikariCP:7.0.2")
//    api("studio.mevera:imperat-core:2.2.0")
    api("io.github.revxrsal:lamp.common:4.0.0-rc.14")

    testImplementation("org.junit.jupiter:junit-jupiter:6.0.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.1")
    testImplementation("org.spongepowered:configurate-gson:4.2.0-GeyserMC-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
}