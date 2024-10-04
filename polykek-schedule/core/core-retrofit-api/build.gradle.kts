plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.android.core.retrofit.api"
    generalConfigurationForLibs(getProject())
}

kotlin {
    configureKotlinOptions()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    // Dagger.
    addDaggerDependencies()

    // Retrofit.
    implementation("com.squareup.retrofit2:converter-gson:$retrofit2Version")
    implementation("com.squareup.retrofit2:converter-simplexml:$retrofit2Version")

    implementation(project(commonModelsModule))
    implementation(project(moduleInjectorModule))
    implementation(project(sharedCodeModule))
}