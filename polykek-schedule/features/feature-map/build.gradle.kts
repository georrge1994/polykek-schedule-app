plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.android.feature.map"
    generalConfigurationForLibs(getProject(), withViewBinding = true)

    defaultConfig {
        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
            abiFilters.add("x86")
            abiFilters.add("x86_64")
        }
    }
}

kotlin {
    configureKotlinOptions()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation("androidx.legacy:legacy-support-v4:$legacySupportV4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")

    // Ktx.
    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleKtxVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleKtxVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleKtxVersion")
    implementation("androidx.fragment:fragment-ktx:$fragmentKtxVersion")
    implementation("androidx.activity:activity-ktx:$activityKtxVersion")
    implementation("androidx.recyclerview:recyclerview:$recyclerViewVersion")

    // Material design.
    implementation("com.google.android.material:material:$materialVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintlayoutVersion")

    // Dagger.
    addDaggerDependencies()

    // Navigation.
    implementation("com.github.terrakok:cicerone:$ciceroneVersion")

    // Yandex map.
    implementation("com.yandex.android:maps.mobile:$yandexMapKitVersion")

    // Other helpful libs.
    implementation("com.codesgood:justifiedtextview:$justifiedTextviewVersion")
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:$bindingDelegateVersion")
    implementation("com.intuit.sdp:sdp-android:$sdpAndSspVersion")
    implementation("com.intuit.ssp:ssp-android:$sdpAndSspVersion")

    // Unit tests.
    addJUnitDependencies()
    addAndroidJUnitDependencies()

    // Core modules.
    implementation(project(coreUiApiModule))
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