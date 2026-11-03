import dev.kikugie.stonecutter.controller.flag.StonecutterFlag

plugins {
    id("dev.kikugie.stonecutter")
}

// Route IDE source generation through Loom's sync task for Gradle 9.8 compatibility
stonecutter.flags[StonecutterFlag.GENERATE_SOURCES_ON_SYNC] = false

stonecutter active "26.2"

val chiseledBuild = tasks.register("chiseledBuild") {
    group = "build"
    description = "Builds every registered Minecraft version."
    dependsOn(stonecutter.tasks.named("build"))
}

tasks.register("buildAll") {
    group = "build"
    description = "Builds every registered Minecraft version."
    dependsOn(chiseledBuild)
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
    dependsOn(stonecutter.tasks.named("clean"))
}

listOf("build", "runClient").forEach { name ->
    tasks.register("${name}Active") {
        group = "stonecutter"
        description = "Runs $name on the active Stonecutter version."
        dependsOn(":${stonecutter.current?.project}:$name")
    }
}
