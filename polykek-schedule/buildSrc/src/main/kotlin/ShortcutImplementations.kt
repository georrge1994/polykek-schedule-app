
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

/**
 * Configures the general library configuration for the given Android library extension.
 */
fun LibraryExtension.generalConfigurationForLibs(
    project: org.gradle.api.Project,
    withViewBinding: Boolean? = null,
    withCompose: Boolean? = null
) {
    compileSdk = COMPILE_SDK
    ndkVersion = NDK_VERSION

    defaultConfig {
        minSdk = MIN_SDK
        lint.targetSdk = TARGET_SDK
        testOptions.targetSdk = TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        viewBinding = withViewBinding ?: false
        compose = withCompose ?: false
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    packaging {
        resources.excludes.add("META-INF/*")
    }
    // That's excluding the second module for the androidTestImplementation configuration.
    @Suppress("UnstableApiUsage")
    project.configurations {
        getByName("androidTestImplementation") {
            exclude(group = "io.mockk", module = "mockk-agent-jvm")
        }
    }
}

/**
 * Configures the Kotlin options for the given Kotlin Android project extension.
 */
fun KotlinAndroidProjectExtension.configureKotlinOptions() {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.addAll(
            listOf(
                "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.ObsoleteCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview"
            )
        )
    }
}

/**
 * Adds the Compose dependencies.
 */
fun DependencyHandler.addComposeDependencies() {
    val composeBom = platform("androidx.compose:compose-bom:$composeBomVersion")
    add("implementation", composeBom)
    add("implementation", "androidx.compose.ui:ui")
    add("implementation", "androidx.compose.material:material")
    add("implementation", "androidx.compose.ui:ui-tooling-preview")
    add("implementation", "androidx.lifecycle:lifecycle-runtime-compose")
    add("debugImplementation", "androidx.compose.ui:ui-tooling")
    add("implementation", "androidx.compose.material:material-icons-core")

    add("implementation", "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleKtxVersion")
    add("implementation", "io.coil-kt:coil-compose:$coilVersion")
}

/**
 * Adds the Dagger dependencies to the given dependency handler.
 */
fun DependencyHandler.addDaggerDependencies() {
    add("implementation", "com.google.dagger:dagger:$daggerVersion")
    add("ksp", "com.google.dagger:dagger-compiler:$daggerVersion")
    add("implementation", "com.google.dagger:dagger-android-support:$daggerVersion")
    add("ksp", "com.google.dagger:dagger-android-processor:$daggerVersion")
    add("ksp", "org.jetbrains.kotlinx:kotlinx-metadata-jvm:$daggerSupportedKotlinMetaDataVersion")
}

/**
 * Adds the Room dependencies to the given dependency handler.
 */
fun DependencyHandler.addRoomDependencies() {
    add("implementation", "androidx.room:room-runtime:$roomVersion")
    add("implementation", "androidx.room:room-ktx:$roomVersion")
    add("ksp", "androidx.room:room-compiler:$roomVersion")
    add("androidTestImplementation", "androidx.room:room-testing:$roomVersion")
}

/**
 * Adds the Firebase dependencies to the given dependency handler.
 */
fun DependencyHandler.addFirebaseDependencies() {
    add("implementation", platform("com.google.firebase:firebase-bom:$firebaseBom"))
    add("implementation", "com.google.firebase:firebase-analytics-ktx")
    add("implementation", "com.google.firebase:firebase-crashlytics")
    add("implementation", "com.google.firebase:firebase-messaging")
}

/**
 * Adds the JUnit test libraries to the given dependency handler.
 */
fun DependencyHandler.addJUnitDependencies() {
    // *************** JUnit tests libs *******************
    add("testImplementation", "junit:junit:$junitVersion")
    add("testImplementation", "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    add("testImplementation", "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    add("testImplementation", "com.google.truth:truth:$truthVersion")
    add("testImplementation", "io.mockk:mockk:$mockkVersion")
    add("testImplementation", "org.mockito:mockito-core:$mockitoVersion")
    add("testImplementation", "androidx.test:core:$testCoreVersion")
    add("testImplementation", "androidx.room:room-testing:$roomVersion")
    // Helper for other arch dependencies, including JUnit test rules that can be used with LiveData.
    add("testImplementation", "androidx.arch.core:core-testing:$coreTestingVersion")
    // For retrofit api testing.
    add("testImplementation", "com.squareup.okhttp3:mockwebserver:$mockWebServerVersion")
    add("testImplementation", "org.json:json:$testJsonVersion")
}

/**
 * Adds the Android JUnit test libraries to the given dependency handler.
 */
fun DependencyHandler.addAndroidJUnitDependencies() {
    // *************** JUnit tests libs *******************
    add("androidTestImplementation", "androidx.arch.core:core-testing:$coreTestingVersion")
    add("androidTestImplementation", "androidx.test:rules:$rulesVersion")
    add("androidTestImplementation", "androidx.test:runner:$testRunnerVersion")
    add("androidTestImplementation", "androidx.test.ext:junit:$androidJunitVersion")
    add("androidTestImplementation", "androidx.test.espresso:espresso-core:$espressoCoreVersion")
    add("androidTestImplementation", "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    add("androidTestImplementation", "io.mockk:mockk-android:$mockkVersion")
    add("androidTestImplementation", "androidx.work:work-testing:$workRuntimeKtxVersion")
}

/**
 * Adds the UI unit dependencies to the given dependency handler.
 */
fun DependencyHandler.addUiUnitDependencies() {
    val composeBom = platform("androidx.compose:compose-bom:$composeBomVersion")
    add("androidTestImplementation", composeBom)
    add("androidTestImplementation", "androidx.compose.ui:ui-test-junit4")
    add("debugImplementation", "androidx.compose.ui:ui-test-manifest")
    add("testImplementation", "app.cash.paparazzi:paparazzi:$paparazziVersion")
}

/**
 * Resolves library conflicts.
 */
fun DependencyHandler.resolveLibraryConflicts() {
    modules {
        module("com.google.guava:listenablefuture") {
            replacedBy("com.google.guava:guava", "listenablefuture is part of guava")
        }
    }
}