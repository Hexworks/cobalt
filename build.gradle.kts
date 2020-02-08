plugins {
    kotlin("multiplatform") apply false
}

allprojects {

    // The name of the git tag, if the current build is tagged.
    val tagName = System.getenv("CIRCLE_TAG")
    val isTag = tagName != null && tagName.isNotBlank()

    // This is generated by Circle CI
    val buildNumber = System.getenv("CIRCLE_BUILD_NUM") ?: "0"

    group = "org.hexworks.cobalt"
    version = "2020.0.$buildNumber${if (isTag) "-RELEASE" else "-SNAPSHOT"}"

    repositories {
        mavenCentral()
        jcenter()
        kotlinx()
        jitpack()
        ktor()
        mavenLocal()
    }

}

subprojects {

    val emptySourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
    }

    val emptyJavadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
    }
}
