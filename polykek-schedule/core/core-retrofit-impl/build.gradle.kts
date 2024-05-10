plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.android.core.retrofit.impl"
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

    // JUnit test libs.
    addJUnitDependencies()

    // Modules.
    implementation(project(coreRetrofitApiModule))
    implementation(project(moduleInjectorModule))

    implementation(project(sharedCodeModule))
    testImplementation(project(testSupportModule))
    androidTestImplementation(project(testSupportModule))
    testImplementation(project(commonModelsModule))

    // Resolve library conflicts.
    resolveLibraryConflicts()
}