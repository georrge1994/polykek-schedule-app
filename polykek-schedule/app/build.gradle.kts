plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = COMPILE_SDK
    namespace = "argument.twins.com.polykekschedule"
    ndkVersion = NDK_VERSION

    defaultConfig {
        applicationId = "argument.twins.com.polykekschedule"
        minSdk = MIN_SDK
        targetSdk = TARGET_SDK
        versionCode = 44
        versionName = "2.3.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true

        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // That excludes module duplicates during gradle builds.
    @Suppress("UnstableApiUsage")
    configurations {
        getByName("androidTestImplementation") {
            exclude(group = "io.mockk", module = "mockk-agent-jvm")
        }
    }

    kotlinOptions {
        jvmTarget = "17"
        // Hide warnings.
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.coroutines.ObsoleteCoroutinesApi",
            "-opt-in=kotlinx.coroutines.FlowPreview"
        )
    }
    packaging {
        resources.excludes.add("META-INF/*")
    }
    // TODO: check that room schemas is generating.
    sourceSets {
        getByName("androidTest") {
            assets.srcDirs("$projectDir/schemas")
        }
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
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

    // Room db.
    addRoomDependencies()

    // Retrofit.
    implementation("com.squareup.retrofit2:converter-gson:$retrofit2Version")

    // Navigation.
    implementation("com.github.terrakok:cicerone:$ciceroneVersion")

    // Other helpful libs.
    implementation("com.codesgood:justifiedtextview:$justifiedTextviewVersion")
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:$bindingDelegateVersion")
    implementation("androidx.core:core-splashscreen:$splashscreenVersion")
    implementation("com.intuit.sdp:sdp-android:$sdpAndSspVersion")
    implementation("com.intuit.ssp:ssp-android:$sdpAndSspVersion")
    implementation("com.google.android.play:core:$playCoreVersion")

    // Firebase.
    addFirebaseDependencies()

    // Yandex map.
    implementation("com.yandex.android:maps.mobile:$yandexMapKitVersion")

    // Unit tests.
    addJUnitDependencies()
    addAndroidJUnitDependencies()

    // Core modules.
    implementation(project(coreUiApiModule))
    implementation(project(coreRoomApiModule))
    implementation(project(coreRetrofitApiModule))
    implementation(project(coreRetrofitImplModule))
    implementation(project(scheduleControllerApiModule))
    implementation(project(scheduleControllerImplModule))

    // Features.
    implementation(project(featureWelcomeModule))
    implementation(project(featureSchoolsModule))
    implementation(project(featureGroupsModule))
    implementation(project(featureMainModule))
    implementation(project(featureScheduleModule))
    implementation(project(featureNotesModule))
    implementation(project(featureMapModule))
    implementation(project(featureBuildingsModule))
    implementation(project(featureProfessorsModule))
    implementation(project(featureFaqModule))
    implementation(project(featureFeedbackModule))

    // Shared modules.
    implementation(project(commonModelsModule))
    implementation(project(sharedCodeModule))
    implementation(project(moduleInjectorModule))
    androidTestImplementation(project(testSupportModule))
    testImplementation(project(testSupportModule))
}