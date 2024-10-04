// This is a special file that is used to define the build configuration for the project.
// https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources
plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.20")
    implementation("com.android.tools.build:gradle:8.6.1")
}
