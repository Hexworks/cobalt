import Libraries.kotlinLogging
import Libraries.kotlinReflect
import Libraries.kotlinTestAnnotationsCommon
import Libraries.kotlinTestCommon
import Libraries.kotlinTestJs
import Libraries.kotlinTestJunit
import Libraries.kotlinxCollectionsImmutable
import Libraries.kotlinxCoroutines
import Libraries.uuid

plugins {
    kotlin("multiplatform")
    id("maven-publish")
    id("signing")
}

kotlin {

//    explicitApi()

    jvm {
        compilations.all {
            kotlinOptions {
                apiVersion = "1.5"
                languageVersion = "1.5"
            }
        }
    }

    js(BOTH) {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlinxCoroutines)
                api(kotlinReflect)
                api(kotlinxCollectionsImmutable)

                api(uuid)
                api(kotlinLogging)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlinTestCommon)
                implementation(kotlinTestAnnotationsCommon)
            }
        }
        val jvmMain by getting {}
        val jvmTest by getting {
            dependencies {
                implementation(kotlinTestJunit)
            }
        }

        val jsMain by getting {}
        val jsTest by getting {
            dependencies {
                implementation(kotlinTestJs)
            }
        }
    }
}

publishing {
    publishWith(
        project = project,
        module = "cobalt.core",
        desc = "Multiplatform utilities library for Kotlin."
    )
}

signing {
    isRequired = false
    sign(publishing.publications)
}
