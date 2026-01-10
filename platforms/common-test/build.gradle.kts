plugins {
    id("java")
}

group = "me.webhead1104.towncraft"

repositories {
    mavenCentral()
}

dependencies {
    impl(project(":platforms:common"))
    impl(libs.logback)
    annotationProcessor(libs.autoService.processor)

    testAnnotationProcessor(libs.autoService.processor)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}