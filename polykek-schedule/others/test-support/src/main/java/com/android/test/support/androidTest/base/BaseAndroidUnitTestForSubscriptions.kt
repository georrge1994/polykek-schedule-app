package com.android.test.support.androidTest.base

import com.android.test.support.androidTest.BaseAndroidUnitTest
import com.android.test.support.androidTest.utils.collectPostScoped
import com.android.test.support.androidTest.utils.getOrAwaitValueScoped
import com.android.test.support.androidTest.utils.getOrAwaitValuesScoped
import com.android.test.support.testFixtures.ONE_SECOND
import com.android.test.support.testFixtures.subscribeAndCompareFirstValueWithScope
import com.android.test.support.testFixtures.subscribeAndCompareSecondValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import java.util.concurrent.TimeoutException

/**
 * Base class for testing subscriptions.
 *
 * @constructor Create empty constructor for base unit test for subscriptions
 */
abstract class BaseAndroidUnitTestForSubscriptions : BaseAndroidUnitTest() {
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

    /**
     * Subscribe and compare second value - actual for state flow.
     *
     * @receiver [StateFlow]
     * @param T Type of channel
     * @param expectedValue Expected value
     * @return [Job]
     */
    protected fun <T> StateFlow<T>.subscribeAndCompareSecondValue(
        expectedValue: T
    ): Job = this.subscribeAndCompareSecondValue(
        expectedValue,
        unconfinedTestDispatcher,
        testScope
    )

    /**
     * Emit and wait response ignore initial value.
     *
     * @receiver [MutableStateFlow]
     * @param T Type of parameter
     * @param value Value
     * @return [Job]
     */
    protected suspend fun <T> MutableStateFlow<T>.emitAndWaitIgnoreInitValue(value: T): Job =
        this.subscribeAndCompareSecondValue(
            value,
            unconfinedTestDispatcher,
            testScope
        ).also {
            this.emit(value)
        }

    /**
     * Get or await value.
     *
     * @receiver [Flow]
     * @param T Type
     * @param time Time in milliseconds
     * @return [T]
     */
    protected fun <T> Flow<T>.getOrAwaitValue(
        expectedResult: T,
        time: Long = 2 * ONE_SECOND
    ): Boolean = this.getOrAwaitValueScoped(expectedResult, testScope, time)

    /**
     * Get or await value.
     *
     * @receiver [Flow]
     * @param T Type
     * @param time Time in milliseconds
     * @return [T]
     */
    @Throws(TimeoutException::class)
    suspend fun <T> Flow<T>.getOrAwaitValue(time: Long = 2 * ONE_SECOND): T =
        this@getOrAwaitValue.getOrAwaitValueScoped(testScope, time)

    /**
     * Get or await values.
     *
     * @receiver [Flow]
     * @param T Type
     * @param count Count of values
     * @param action Action after observe
     * @param time Time in milliseconds
     * @return [T]
     */
    suspend fun <T> Flow<T>.getOrAwaitValues(
        count: Int,
        action: (suspend () -> Unit)? = null,
        time: Long = 2 * ONE_SECOND
    ): List<T> = getOrAwaitValuesScoped(count, action, testScope, time)

    /**
     * Collect post with expecting count of items.
     *
     * @receiver [Flow]
     * @param T T
     * @param count Count of expected values
     * @param timeout Timeout before throw exception
     * @param action Action
     * @return List of caught items
     */
    @Throws(TimeoutException::class)
    suspend fun <T> Flow<T>.collectPost(
        count: Int = 0,
        timeout: Long = 2 * ONE_SECOND,
        action: suspend () -> Unit
    ): List<T?> = collectPostScoped(count, timeout, testScope, action)
}