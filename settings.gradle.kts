pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

plugins {
    id("com.possible-triangle.helper") version ("1.2")
}

include("common", "fabric", "forge")
