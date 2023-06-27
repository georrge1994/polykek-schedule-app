package com.android.test.support.androidTest.utils

import com.android.test.support.testFixtures.ONE_SECOND
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.TestCoroutineScope
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Helper method for testing State objects, from
 *
 * Get the value from a Flow. We're waiting for 2 seconds before throw exception.
 * Once we got a notification via onChanged, we stop observing.
 */

/**
 * Get or await value.
 *
 * @receiver [Flow]
 * @param T Type
 * @param testScope [TestCoroutineScope]
 * @param time Time in milliseconds
 * @return [T]
 */
fun <T> Flow<T>.getOrAwaitValueScoped(
    expectedResult: T,
    testScope: CoroutineScope,
    time: Long = 2 * ONE_SECOND
): Boolean {
    val latch = CountDownLatch(1)
    var result: T? = null
    val job = this.onEach { o ->
        result = o
        if (o == expectedResult)
            latch.countDown()
    }.launchIn(testScope)
    // Don't wait indefinitely if the Flow is not set.
    if (!latch.await(time, TimeUnit.MILLISECONDS)) {
        job.cancel()
        throw TimeoutException("Expected value was never set, result = $result")
    }

    job.cancel()
    return true
}

/**
 * Get or await value.
 *
 * @receiver [Flow]
 * @param T Type
 * @param testScope [TestCoroutineScope]
 * @param time Time in milliseconds
 * @return [T]
 */
@Throws(TimeoutException::class)
suspend fun <T> Flow<T>.getOrAwaitValueScoped(testScope: CoroutineScope, time: Long = 2 * ONE_SECOND): T =
    getOrAwaitValuesScoped(1, null, testScope, time).first()

/**
 * Get or await values.
 *
 * @receiver [Flow]
 * @param T Type
 * @param count Count of values
 * @param action Action after observe
 * @param testScope [TestCoroutineScope]
 * @param time Time in milliseconds
 * @return [T]
 */
@Throws(TimeoutException::class)
suspend fun <T> Flow<T>.getOrAwaitValuesScoped(
    count: Int,
    action: (suspend () -> Unit)? = null,
    testScope: CoroutineScope,
    time: Long = 2 * ONE_SECOND
): List<T> {
    val data = ArrayList<T>()
    val latch = CountDownLatch(count)
    val job = this.onEach { o ->
        data.add(o)
        latch.countDown()
    }.launchIn(testScope)
    action?.invoke()
    // Don't wait indefinitely if the Flow is not set.
    @Suppress("BlockingMethodInNonBlockingContext")
    if (!latch.await(time, TimeUnit.MILLISECONDS)) {
        job.cancel()
        throw TimeoutException("Flow value was never set.")
    }
    job.cancel()
    return data
}

/**
 * Collect post with expecting count of items.
 *
 * @receiver [Flow]
 * @param T T
 * @param count Count of expected values
 * @param testScope [TestCoroutineScope]
 * @param timeout Timeout before throw exception
 * @param action Action
 * @return List of caught items
 */
@Throws(TimeoutException::class)
suspend fun <T> Flow<T>.collectPostScoped(
    count: Int = 0,
    timeout: Long = 2 * ONE_SECOND,
    testScope: CoroutineScope,
    action: suspend () -> Unit
): List<T?> {
    val items = ArrayList<T?>()
    val isCountingEnabled = count > 0
    val latch = CountDownLatch(count)
    val job = this.onEach { o ->
        items.add(o)
        if (isCountingEnabled)
            latch.countDown()
    }.launchIn(testScope)
    action.invoke()
    @Suppress("BlockingMethodInNonBlockingContext")
    if (isCountingEnabled && !latch.await(timeout, TimeUnit.MILLISECONDS)) {
        job.cancel()
        throw TimeoutException("Flow not all values were never set: ${items.joinToString()}.")
    }
    job.cancel()
    return items
}