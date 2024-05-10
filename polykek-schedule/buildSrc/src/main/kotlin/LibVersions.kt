
const val MIN_SDK = 21
const val COMPILE_SDK = 34
const val TARGET_SDK = 34
const val NDK_VERSION = "26.2.11394342"

const val legacySupportV4 = "1.0.0"
const val appcompatVersion = "1.6.1"

const val coreKtxVersion = "1.13.1"
const val coroutinesVersion = "1.8.0"
const val lifecycleKtxVersion = "2.7.0"
const val splashscreenVersion = "1.0.1"
const val fragmentKtxVersion = "1.7.0"
const val activityKtxVersion = "1.9.0"
const val recyclerViewVersion = "1.3.2"

const val materialVersion = "1.12.0"
const val constraintlayoutVersion = "2.1.4"

const val retrofit2Version = "2.11.0"
const val roomVersion = "2.6.1"
const val daggerVersion = "2.50"
// Daggers uses old version of kotlinx-metadata-jvm library to parse kotlin metadata. It doesn"t support kotlin version 1.7.x yet.
const val daggerSupportedKotlinMetaDataVersion = "0.9.0"
const val ciceroneVersion = "7.1"
const val firebaseBom = "32.7.3"

const val justifiedTextviewVersion = "2.0.1"
const val bindingDelegateVersion = "1.5.9"
const val sdpAndSspVersion = "1.1.0"
const val playCoreVersion = "1.10.3"

const val yandexMapKitVersion = "4.2.2-lite"

const val junitVersion = "4.13.2"
const val androidJunitVersion = "1.1.5"
const val testRunnerVersion = "1.5.2"
const val espressoCoreVersion = "3.5.1"
const val truthVersion = "1.4.2"
const val mockkVersion = "1.13.10"
const val mockitoVersion = "5.10.0"
const val mockWebServerVersion = "5.0.0-alpha.12"
const val testCoreVersion = "1.5.0"
const val coreTestingVersion = "2.2.0"
const val testJsonVersion = "20231013"
const val rulesVersion = "1.5.0"
const val workRuntimeKtxVersion = "2.9.0"

// Core.
const val coreUiApiModule = ":core:core-ui"
const val coreRoomApiModule = ":core:core-room-api"
const val coreRetrofitApiModule = ":core:core-retrofit-api"
const val coreRetrofitImplModule = ":core:core-retrofit-impl"
const val scheduleControllerApiModule = ":core:schedule-controller-api"
const val scheduleControllerImplModule = ":core:schedule-controller-impl"

// Features.
const val featureScheduleModule = ":features:feature-schedule"
const val featureFaqModule = ":features:feature-faq"
const val featureFeedbackModule = ":features:feature-feedback"
const val featureWelcomeModule = ":features:feature-welcome"
const val featureSchoolsModule = ":features:feature-schools"
const val featureGroupsModule = ":features:feature-groups"
const val featureMainModule = ":features:feature-main-screen"
const val featureNotesModule = ":features:feature-notes"
const val featureBuildingsModule = ":features:feature-buildings"
const val featureMapModule = ":features:feature-map"
const val featureProfessorsModule = ":features:feature-professors"

// Shared modules.
const val moduleInjectorModule = ":others:module-injector"
const val sharedCodeModule = ":others:shared-code"
const val commonModelsModule = ":others:common-models"
const val testSupportModule = ":others:test-support"