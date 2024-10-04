plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
    id("app.cash.paparazzi") version "1.3.4"
}

android {
    namespace = "com.android.feature.web.content"
    generalConfigurationForLibs(getProject(), withCompose = true)
}

kotlin {
    configureKotlinOptions()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.legacy:legacy-support-v4:$legacySupportV4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("androidx.core:core-ktx:$coreKtxVersion")

    // Compose.
    addComposeDependencies()

    // Dagger.
    addDaggerDependencies()
    
    // Navigation.
    implementation("com.github.terrakok:cicerone:$ciceroneVersion")

    // Unit tests.
    addJUnitDependencies()
    addAndroidJUnitDependencies()
    addUiUnitDependencies()

    // Core modules.
    implementation(project(coreUiApiModule))
    implementation(project(coreRetrofitApiModule))

    // Shared modules.
    implementation(project(sharedCodeModule))
    implementation(project(commonModelsModule))
    implementation(project(moduleInjectorModule))
    androidTestImplementation(project(testSupportModule))
    testImplementation(project(testSupportModule))

    // Resolve library conflicts.
    resolveLibraryConflicts()
}