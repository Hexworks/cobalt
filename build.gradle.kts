allprojects {

    repositories {
        
    }
}
allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        kotlinx()
        kotlinEap()
        jitpack()
        maven("https://dl.bintray.com/kotlin/kotlinx")
    }
}

subprojects {
    apply<MavenPublishPlugin>()
}
