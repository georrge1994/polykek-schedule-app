package com.android.test.support.androidTest.sdk

/**
 * Test for target SDK and all SDK after it.
 *
 * @property sdkLevel [SdkLevel]
 * @constructor Create [TestForTargetSDKAndAbove]
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestForTargetSDKAndAbove(val sdkLevel: SdkLevel)