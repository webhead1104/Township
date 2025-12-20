plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "3.2.0"
}

group = "me.webhead1104"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("me.webhead1104.menuDesigner.Launcher")
}

javafx {
    version = "21.0.9"
    modules = listOf("javafx.fxml", "javafx.web")
}

dependencies {
    implementation("net.kyori:adventure-text-minimessage:4.26.1")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("org.slf4j:slf4j-api:2.1.0-alpha1")
    implementation("ch.qos.logback:logback-classic:1.5.22")
}