rootProject.name = "Towncraft"
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include("tools:wiki-scraper", "tools:menu-designer")
include(":platforms:common")
include(":platforms:cytosis")
include(":platforms:paper")