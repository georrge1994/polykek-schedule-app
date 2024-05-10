plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.android.core.ui"
    generalConfigurationForLibs(getProject(), withViewBinding = true)
}

kotlin {
    configureKotlinOptions()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.legacy:legacy-support-v4:$legacySupportV4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("androidx.core:core-splashscreen:$splashscreenVersion")

    // Ktx.
    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleKtxVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleKtxVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleKtxVersion")
    implementation("androidx.recyclerview:recyclerview:$recyclerViewVersion")

    // Dagger.
    addDaggerDependencies()

    // Material design.
    implementation("com.google.android.material:material:$materialVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintlayoutVersion")

    // Navigation.
    implementation("com.github.terrakok:cicerone:$ciceroneVersion")

    // Other helpful libs.
    implementation("com.intuit.sdp:sdp-android:$sdpAndSspVersion")
    implementation("com.intuit.ssp:ssp-android:$sdpAndSspVersion")

    // Unit tests.
    addJUnitDependencies()
    addAndroidJUnitDependencies()

    // Shared modules.
    implementation(project(sharedCodeModule))
    implementation(project(moduleInjectorModule))
    androidTestImplementation(project(testSupportModule))
    testImplementation(project(testSupportModule))

    // Resolve library conflicts.
    resolveLibraryConflicts()
}