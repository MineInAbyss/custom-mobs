pluginManagement {
    val kotlinVersion: String by settings
    val kspVersion: String by settings

    repositories {
        gradlePluginPortal()
        google()
        maven("https://repo.mineinabyss.com/releases")
    }

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        id("com.google.devtools.ksp") version kspVersion
    }
}

rootProject.name = "mobzy"

include("processor")
