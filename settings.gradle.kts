pluginManagement {
    repositories {
        mavenCentral()

        gradlePluginPortal()
    }

    plugins {
        kotlin("jvm") version "1.8.10" apply false
        id("org.openjfx.javafxplugin") version "0.0.14" apply false
    }
}

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

rootProject.name = "FDEngine"
include("Application", "Engine", "EngineFramework", "Tools")
