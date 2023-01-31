package com.android.test.support.androidTest.sdk

/**
 * Test only for target SDK.
 *
 * @property sdkLevel [SdkLevel]
 * @constructor Create [TestOnlyForTargetSDK]
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestOnlyForTargetSDK(val sdkLevel: SdkLevel)