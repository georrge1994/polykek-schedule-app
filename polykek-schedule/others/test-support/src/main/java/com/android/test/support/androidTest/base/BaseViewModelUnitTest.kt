package com.android.test.support.androidTest.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.test.support.androidTest.utils.TestCoroutineRule
import io.mockk.MockKAnnotations
import org.junit.Rule

/**
 * Base view model unit test.
 *
 * @constructor Create empty constructor for base view model unit test
 */
abstract class BaseViewModelUnitTest : BaseAndroidUnitTestForSubscriptions() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineScopeRule = TestCoroutineRule(unconfinedTestDispatcher)

    // Override super setMain dispatcher modifications.
    override fun beforeTest() {
        MockKAnnotations.init(this, relaxed = true)
    }

    // Override super resetMain dispatcher modifications.
    override fun afterTest() { }
}