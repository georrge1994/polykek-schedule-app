package com.android.test.support.unitTest.base

import com.android.test.support.testFixtures.subscribeAndCompareFirstValueWithScope
import com.android.test.support.unitTest.BaseUnitTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Base class for testing subscriptions.
 *
 * @constructor Create empty constructor for base unit test for subscriptions
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseUnitTestForSubscriptions : BaseUnitTest() {
    protected val unconfinedTestDispatcher = UnconfinedTestDispatcher()
    protected val testScope = TestScope(unconfinedTestDispatcher)

    override fun beforeTest() {
        super.beforeTest()
        Dispatchers.setMain(unconfinedTestDispatcher)
    }

    override fun afterTest() {
        super.afterTest()
        Dispatchers.resetMain()
    }

    /**
     * Emit and wait response.
     *
     * @receiver [MutableStateFlow]
     * @param T Type of parameter
     * @param value Value
     * @return [Job]
     */
    protected suspend fun <T> MutableSharedFlow<T>.emitAndWait(value: T): Job {
        if (this !is MutableStateFlow)
            this.resetReplayCache()
        return this.subscribeAndCompareFirstValue(value).also {
            this.emit(value)
        }
    }

    /**
     * Subscribe and compare first value.
     *
     * @receiver [MutableSharedFlow]
     * @param T Type of channel
     * @param expectedValue Expected value
     * @return [Job]
     */
    protected fun <T> Flow<T>.subscribeAndCompareFirstValue(
        expectedValue: T
    ): Job = this.subscribeAndCompareFirstValueWithScope(
        expectedValue,
        unconfinedTestDispatcher,
        testScope
    )
}