plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.android.test.support"
    generalConfigurationForLibs(getProject())
}

kotlin {
    configureKotlinOptions()
}

dependencies {
    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("com.google.android.material:material:$materialVersion")

    // Retrofit.
    implementation("com.squareup.retrofit2:converter-gson:$retrofit2Version")

    // *************** JUnit tests libs *******************
    implementation("junit:junit:$junitVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    implementation("com.google.truth:truth:$truthVersion")
    implementation("io.mockk:mockk:$mockkVersion")
    implementation("androidx.test:core:$testCoreVersion")
    implementation("androidx.room:room-testing:$roomVersion")
    // Helper for other arch dependencies, including JUnit test rules that can be used with LiveData.
    implementation("androidx.arch.core:core-testing:$coreTestingVersion")
    // For retrofit api testing.
    implementation("com.squareup.okhttp3:mockwebserver:$mockWebServerVersion")

    // *************** Android JUnit tests *******************
    implementation("androidx.test:rules:$rulesVersion")
    implementation("androidx.test:runner:$testRunnerVersion")
    implementation("androidx.test.ext:junit:$androidJunitVersion")
    implementation("androidx.test.espresso:espresso-core:$espressoCoreVersion")
    implementation("androidx.work:work-testing:$workRuntimeKtxVersion")

    // Compose UI tests.
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui-test-junit4")
    implementation("androidx.compose.ui:ui-test")
    // TODO: try to fix it. It ruins the build, cannot create BasePaparazziTest.
//    implementation("app.cash.paparazzi:paparazzi:$paparazziVersion")

    implementation(project(commonModelsModule))
}