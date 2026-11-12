import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("net.fabricmc.fabric-loom")
    id("maven-publish")
    idea
}

val minecraftVersion = stonecutter.current.version
val targetJavaVersion = 25
val noammAddonsVersion = project.property("noammaddons_version") as String
val noammAddonsType = project.property("noammaddons_type") as String
val modName = project.property("mod_name") as String
val modId = project.property("mod_id") as String

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(modName)
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

if (System.getProperty("idea.sync.active").toBoolean() && !stonecutter.current.isActive) {
    sourceSets.main {
        java.setSrcDirs(emptyList<File>())
        kotlin.setSrcDirs(emptyList<File>())
    }
}

tasks.named("ideaSyncTask") {
    dependsOn("stonecutterGenerate", "stonecutterGenerateTest")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}

repositories {
    maven { url = uri("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") }
    exclusiveContent {
        forRepository {
            ivy("https://github.com/Noamm9/NoammAddons/releases/download/") {
                name = "NoammAddonsReleases"
                patternLayout {
                    artifact("[revision]/[artifact]-[revision]-[classifier].[ext]")
                }
                metadataSources { artifact() }
            }
        }
        filter { includeModule("com.github.Noamm9", "NoammAddons") }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    implementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    implementation("net.fabricmc:fabric-language-kotlin:${project.property("fabric_kotlin_version")}")

    implementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")

    runtimeOnly("me.djtheredstoner:DevAuth-fabric:${project.property("devauth_version")}")

    implementation("com.github.Noamm9:NoammAddons:$noammAddonsVersion:$minecraftVersion-$noammAddonsType") {
        isTransitive = false
    }
}

val resourceProperties = mapOf(
    "version" to project.version,
    "mod_name" to modName,
    "mod_id" to modId,
    "minecraft_version" to minecraftVersion,
    "java_version" to targetJavaVersion,
    "loader_version" to project.property("loader_version"),
    "fabric_kotlin_version" to project.property("fabric_kotlin_version"),
    "noammaddons_version" to noammAddonsVersion
)

tasks.processResources {
    inputs.properties(resourceProperties)
    filteringCharset = "UTF-8"

    filesMatching(listOf("fabric.mod.json", "$modId.mixins.json")) {
        expand(resourceProperties)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
    options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

tasks.withType<Jar>().configureEach {
    archiveVersion.set("${project.version}-$minecraftVersion")
}

tasks.jar {
    archiveClassifier.set(noammAddonsType)
    destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = modName
            version = "${project.version}-$minecraftVersion"
            from(components["java"])
        }
    }
}

loom {
    runs.configureEach {
        runDirectory.set(layout.projectDirectory.dir("run"))
        generateRunConfig.set(false)
        jvmArguments.addAll(
            "-XX:+AllowEnhancedClassRedefinition", // This is only available in JBR
            "-XX:+IgnoreUnrecognizedVMOptions",
        )
        jvmArguments.add(provider {
            "-javaagent:${configurations.compileClasspath.get().single { "sponge-mixin" in it.name }}"
        })
    }
}
