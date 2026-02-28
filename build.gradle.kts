import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.include
import org.gradle.kotlin.dsl.mappings
import org.gradle.kotlin.dsl.modImplementation
import org.gradle.kotlin.dsl.modRuntimeOnly
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.10"
    id("fabric-loom") version "1.15-SNAPSHOT"
    id("maven-publish")
    id ("org.jetbrains.kotlin.plugin.serialization") version "2.3.10"
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

val targetJavaVersion = 21
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    withSourcesJar()
}



repositories {
    maven { url = uri("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.property("kotlin_loader_version")}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")

    modRuntimeOnly("me.djtheredstoner:DevAuth-fabric:1.2.2")

    implementation ("io.github.classgraph:classgraph:4.8.174")
    include ("io.github.classgraph:classgraph:4.8.174")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    include("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    modImplementation("com.github.noamm9:noammaddons-1.21.10:${project.property("noammaddons_version")}") {
        isTransitive = false
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand("version" to project.version, "minecraft_version" to project.property("minecraft_version")!!, "loader_version" to project.property("loader_version")!!, "kotlin_loader_version" to project.property("kotlin_loader_version")!!)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }
}

loom {
    runConfigs.named("client") {
        isIdeConfigGenerated = true
        vmArg("-XX:+AllowEnhancedClassRedefinition")
    }

    runConfigs.named("server") {
        isIdeConfigGenerated = false
    }
}

afterEvaluate {
    loom.runs.named("client") {
        vmArg("-javaagent:${configurations.compileClasspath.get().find { "sponge-mixin" in it.name }}")
    }
}