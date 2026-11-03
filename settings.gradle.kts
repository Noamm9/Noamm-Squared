pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases")
    }

    plugins {
        id("dev.kikugie.stonecutter") version providers.gradleProperty("stonecutter_version").get()
        id("net.fabricmc.fabric-loom") version providers.gradleProperty("loom_version").get()
        id("org.jetbrains.kotlin.jvm") version providers.gradleProperty("kotlin_version").get()
    }
}

plugins {
    id("dev.kikugie.stonecutter")
}

rootProject.name = providers.gradleProperty("mod_name").get()

stonecutter {
    create(rootProject) {
        versions("26.2", "26.3")
        vcsVersion = "26.2"
    }
}
