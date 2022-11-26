rootProject.name = "IrisWatcher"

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        val kotlinVersion = extra["kotlinVersion"] as String
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion

        val ktorVersion = extra["ktorVersion"] as String
        id("io.ktor.plugin") version ktorVersion
    }
}
