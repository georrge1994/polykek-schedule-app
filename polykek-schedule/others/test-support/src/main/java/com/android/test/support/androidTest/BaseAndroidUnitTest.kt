package com.android.test.support.androidTest

import android.app.Application
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.android.test.support.androidTest.sdk.ConditionalSDKTestRunner
import com.android.test.support.testFixtures.TEST_STRING
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.fail
import org.junit.Before
import org.junit.runner.RunWith

const val DELTA = 0.01

/**
 * Base android unit test.
 *
 * @constructor Create empty constructor for base android unit test
 */
@RunWith(ConditionalSDKTestRunner::class)
abstract class BaseAndroidUnitTest {
    protected val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val application: Application = mockk {
        coEvery { applicationContext } returns targetContext
        coEvery { packageName } returns "argument.twins.com.polykekschedule"
        coEvery { getString(any()) } returns TEST_STRING
        coEvery { getString(any(), any()) } returns TEST_STRING
    }
    /**
     * Before test.
     */
    @Before
    open fun beforeTest() { }

    /**
     * After test.
     */
    @After
    open fun afterTest() { }

    /**
     * Assert throws.
     *
     * @param T Exception type
     * @param runnable Runnable
     */
    inline fun <reified T : Exception> assertThrows(runnable: () -> Any?) {
        try {
            runnable.invoke()
        } catch (e: Throwable) {
            if (e is T) {
                return
            }
            fail("expected ${T::class.qualifiedName} but caught ${e::class.qualifiedName} instead")
        }
        fail("expected ${T::class.qualifiedName}")
    }
}