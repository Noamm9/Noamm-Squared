pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }

        maven("https://jitpack.io") {
            name = "JitPack"
        }
        gradlePluginPortal()
    }
}
