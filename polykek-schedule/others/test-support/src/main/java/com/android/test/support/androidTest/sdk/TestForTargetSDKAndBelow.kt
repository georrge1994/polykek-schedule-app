package com.android.test.support.androidTest.sdk

/**
 * Test for target SDK and all SDK before it.
 *
 * @property sdkLevel [SdkLevel]
 * @constructor Create [TestForTargetSDKAndBelow]
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestForTargetSDKAndBelow(val sdkLevel: SdkLevel)