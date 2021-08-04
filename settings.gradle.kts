rootProject.name = "testerum-api"

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    }

    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("testerum-steps-api")
