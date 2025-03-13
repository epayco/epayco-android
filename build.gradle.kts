plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("maven-publish")
}

// Configuración del plugin maven-publish
publishing {
    publications {
        create<MavenPublication>("mavenAndroid") {
            afterEvaluate {
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            url = uri("file://${buildDir}/repo")
        }
    }
}