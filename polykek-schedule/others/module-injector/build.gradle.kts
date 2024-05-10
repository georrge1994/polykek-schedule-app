plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.android.shared.injector"
    generalConfigurationForLibs(getProject())
}

kotlin {
    configureKotlinOptions()
}

dependencies {
    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("com.google.android.material:material:$materialVersion")

    // Dagger.
    addDaggerDependencies()
}