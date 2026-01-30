import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "org.hexworks.cobalt"
version = "2.0.0"

kotlin {
    jvm {}
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()
    js(IR) {
        browser {
            testTask {
                useMocha()
            }
        }
        nodejs()
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlin.reflect)
            api(libs.kotlinx.collections.immutable)
            api(libs.kotlin.logging)
        }

        jvmMain.dependencies {
            api(libs.slf4j)
            api(libs.logback)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "cobalt.core", version.toString())

    pom {
        name = "cobalt"
        description = "Multiplatform utilities library for Kotlin"
        inceptionYear = "2018"
        url = "https://github.com/Hexworks/cobalt"
        licenses {
            license {
                name = "The Apache Software License, Version 2.0 "
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "adam-arold"
                name = "Adam Arold"
                url = "https://github.com/adam-arold"
            }
        }
        scm {
            url = "https://github.com/Hexworks/cobalt.git"
            connection = "git@github.com:Hexworks/cobalt.git"
            developerConnection = "git@github.com:Hexworks/cobalt.git"
        }
    }
}
