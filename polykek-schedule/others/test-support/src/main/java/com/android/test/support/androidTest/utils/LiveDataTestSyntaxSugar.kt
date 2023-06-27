package com.android.test.support.androidTest.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.android.test.support.testFixtures.ONE_SECOND
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Helper method for testing LiveData objects, from
 * https://github.com/googlesamples/android-architecture-components.
 *
 * Get the value from a LiveData object. We're waiting for LiveData to emit, for 2 seconds.
 * Once we got a notification via onChanged, we stop observing.
 */

/**
 * Get or await value.
 *
 * @receiver [LiveData]
 * @param T Type
 * @param time Time in milliseconds
 * @return [T]
 */
fun <T> LiveData<T>.getOrAwaitValueScoped(expectedResult: T, time: Long = 2 * ONE_SECOND): Boolean {
    val latch = CountDownLatch(1)
    var result: T? = null
    val observer = Observer<T> { o ->
        result = o
        if (o == expectedResult)
            latch.countDown()
    }
    this.observeForever(observer)
    // Don't wait indefinitely if the LiveData is not set.
    @Suppress("BlockingMethodInNonBlockingContext")
    if (!latch.await(time, TimeUnit.MILLISECONDS)) {
        this.removeObserver(observer)
        throw TimeoutException("Expected value was never set, result = $result")
    }

    this.removeObserver(observer)
    return true
}

/**
 * Get or await value.
 *
 * @receiver [LiveData]
 * @param T Type
 * @param time Time in milliseconds
 * @return [T]
 */
@Throws(TimeoutException::class)
suspend fun <T> LiveData<T>.getOrAwaitValueScoped(time: Long = 2 * ONE_SECOND): T =
    getOrAwaitValuesScoped(1, null, time).first()

/**
 * Get or await values.
 *
 * @receiver [LiveData]
 * @param T Type
 * @param count Count of values
 * @param action Action after observe
 * @param time Time in milliseconds
 * @return [T]
 */
@Throws(TimeoutException::class)
suspend fun <T> LiveData<T>.getOrAwaitValuesScoped(
    count: Int,
    action: (suspend () -> Unit)? = null,
    time: Long = 2 * ONE_SECOND
): List<T> {
    val data = ArrayList<T>()
    val latch = CountDownLatch(count)
    val observer = Observer<T> { o ->
        data.add(o)
        latch.countDown()
    }
    this.observeForever(observer)
    action?.invoke()
    // Don't wait indefinitely if the LiveData is not set.
    @Suppress("BlockingMethodInNonBlockingContext")
    if (!latch.await(time, TimeUnit.MILLISECONDS)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }
    this.removeObserver(observer)
    @Suppress("UNCHECKED_CAST")
    return data
}

/**
 * Collect post with expecting count of items.
 *
 * @receiver [LiveData]
 * @param T T
 * @param count Count of expected values
 * @param timeout Timeout before throw exception
 * @param action Action
 * @return List of caught items
 */
@Throws(TimeoutException::class)
suspend fun <T> LiveData<T>.collectPostScoped(
    count: Int = 0,
    timeout: Long = 2 * ONE_SECOND,
    action: suspend () -> Unit
): List<T?> {
    val items = ArrayList<T?>()
    val isCountingEnabled = count > 0
    val latch = CountDownLatch(count)
    val observer = Observer<T> { o ->
        items.add(o)
        if (isCountingEnabled)
            latch.countDown()
    }
    observeForever(observer)
    action.invoke()
    @Suppress("BlockingMethodInNonBlockingContext")
    if (isCountingEnabled && !latch.await(timeout, TimeUnit.MILLISECONDS)) {
        removeObserver(observer)
        throw TimeoutException("LiveData not all values were never set: ${items.joinToString()}.")
    }
    removeObserver(observer)
    return items
}