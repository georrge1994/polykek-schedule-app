package com.android.test.support.androidTest.sdk

import android.os.Build
import org.junit.runner.notification.RunNotifier
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod

/**
 * Conditional sdk test runner. This class runner helps to check tests specific SDK on specific emulators.
 *
 * @constructor Create [ConditionalSDKTestRunner]
 *
 * @param klass Test klass
 */
class ConditionalSDKTestRunner(klass: Class<*>?) : BlockJUnit4ClassRunner(klass) {
    override fun runChild(method: FrameworkMethod, notifier: RunNotifier) {
        // Annotation class could not have a base class of interface, so we can not group them.
        val testOnlyForTargetSDKCode = method.getAnnotation(TestOnlyForTargetSDK::class.java)?.sdkLevel?.code
        val testForTargetSDKAndBelowCode = method.getAnnotation(TestForTargetSDKAndBelow::class.java)?.sdkLevel?.code
        val testForTargetSDKAndAboveCode = method.getAnnotation(TestForTargetSDKAndAbove::class.java)?.sdkLevel?.code

        when {
            // If annotation exists, but target SDK is not equal of emulator SDK -> skip this test.
            testOnlyForTargetSDKCode != null && testOnlyForTargetSDKCode != Build.VERSION.SDK_INT ->
                notifier.fireTestIgnored(describeChild(method))
            // If annotation exists, but test SDK is lower than emulator SDK -> skip this test.
            testForTargetSDKAndBelowCode != null && testForTargetSDKAndBelowCode < Build.VERSION.SDK_INT ->
                notifier.fireTestIgnored(describeChild(method))
            // If annotation exists, but test SDK is higher than emulator SDK -> skip this test.
            testForTargetSDKAndAboveCode != null && testForTargetSDKAndAboveCode > Build.VERSION.SDK_INT ->
                notifier.fireTestIgnored(describeChild(method))
            // For other cases test should be started.
            else -> super.runChild(method, notifier)
        }
    }
}