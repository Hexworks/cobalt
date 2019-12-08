plugins {
    kotlinMultiplatform
}

group = "org.hexworks.cobalt"

kotlin {

    jvm {
        jvmTarget(JavaVersion.VERSION_1_8)
        withJava()
    }

    js {
    }

    dependencies {

        with(Projects) {
            commonMainApi(cobaltDatatypes)
            commonMainApi(cobaltEvents)
        }

        with(TestLibs) {
            commonTestImplementation(kotlinTestCommon)
            commonTestImplementation(kotlinTestAnnotationsCommon)
            jvmTestImplementation(kotlinTestJunit)
            jsTestImplementation(kotlinTestJs)
        }
    }
}