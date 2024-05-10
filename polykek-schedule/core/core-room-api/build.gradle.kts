plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.android.core.room.api"
    generalConfigurationForLibs(getProject())
}

kotlin {
    configureKotlinOptions()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleKtxVersion")

    // Room db.
    addRoomDependencies()

    // Modules.
    implementation(project(commonModelsModule))
    implementation(project(moduleInjectorModule))
    implementation(project(sharedCodeModule))
}