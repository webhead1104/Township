rootProject.name = "Towncraft"

include("tools:wiki-scraper", "tools:menu-designer")
include(":platforms:common")
include(":platforms:cytosis")
include(":platforms:paper")

//project(":platforms:common").name = "Towncraft-Common"
//project(":platforms:cytosis").name = "Towncraft-Cytosis"
//project(":platforms:paper").name = "Towncraft-Paper"