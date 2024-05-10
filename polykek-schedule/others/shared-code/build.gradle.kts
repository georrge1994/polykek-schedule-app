plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.android.shared.code"
    generalConfigurationForLibs(getProject())
}

kotlin {
    configureKotlinOptions()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    // Ktx.
    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleKtxVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleKtxVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleKtxVersion")
    implementation("androidx.fragment:fragment-ktx:$fragmentKtxVersion")
    implementation("androidx.activity:activity-ktx:$activityKtxVersion")

    // Material design.
    implementation("com.google.android.material:material:$materialVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintlayoutVersion")

    // Dagger.
    addDaggerDependencies()

    // Unit tests.
    addJUnitDependencies()
    addAndroidJUnitDependencies()
}