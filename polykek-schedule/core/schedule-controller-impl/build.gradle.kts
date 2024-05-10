plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.android.schedule.controller.impl"
    generalConfigurationForLibs(getProject())
}

kotlin {
    configureKotlinOptions()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    // Ktx.
    implementation("androidx.core:core-ktx:$coreKtxVersion")

    // Dagger.
    addDaggerDependencies()

    // Retrofit.
    implementation("com.squareup.retrofit2:converter-gson:$retrofit2Version")

    // JUnit test libs.
    addJUnitDependencies()
    addAndroidJUnitDependencies()

    // Core modules.
    implementation(project(coreUiApiModule))
    implementation(project(coreRoomApiModule))
    implementation(project(coreRetrofitApiModule))
    implementation(project(scheduleControllerApiModule))

    // Shared modules.
    implementation(project(sharedCodeModule))
    implementation(project(commonModelsModule))
    implementation(project(moduleInjectorModule))
    androidTestImplementation(project(testSupportModule))
    testImplementation(project(testSupportModule))

    // Resolve library conflicts.
    resolveLibraryConflicts()
}