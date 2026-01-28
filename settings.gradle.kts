pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "cobalt"

include(":cobalt.core")