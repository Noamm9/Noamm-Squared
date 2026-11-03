# NoammAddons addon template

Use this as a starting point for your own NoammAddons addon. It's set up for Kotlin and Minecraft 26.2 / 26.3, using Stonecutter.

## Getting started

1. Click **Use this template**, then clone your repo.
2. Open it as a Gradle project. You'll need JDK 25, also selected as Gradle's JVM in your IDE.
3. Change the mod name, ID and group in `gradle.properties`. Set `noammaddons_type` to `cheat` or `legit`, depending on which version of NoammAddons you want to use.
4. Set `noammaddons_version` in `versions/<version>/gradle.properties` to the NoammAddons release you're building against.
5. Rename the `untitled` packages, entrypoint, assets and mixin config for your mod.

To add another Minecraft version, start in `settings.gradle.kts` and give it its own properties file under `versions/`.

## Build and run

- In IntelliJ, choose `Untitled [buildAll]`, `Untitled [runClient 26.2]` or `Untitled [runClient 26.3]` from the run dropdown. These configs are included in `.run/`.

You'll find the built jars in `build/libs/`. Each Minecraft version has its own `run/` folder under `versions/`.

## Help

If you're stuck, feel free to ask in the [NoammAddons Discord](https://discord.gg/pj9mQGxMxB) or DM `jcnlk` or `noamm99` on Discord.
