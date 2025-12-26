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
    implementation(libs.adventure.minimessage)
    implementation(libs.gson)
    implementation(libs.slf4j)
    implementation(libs.logback)
}