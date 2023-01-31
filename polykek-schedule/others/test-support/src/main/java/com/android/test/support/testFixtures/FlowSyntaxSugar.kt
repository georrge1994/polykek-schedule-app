package com.android.test.support.testFixtures

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Subscribe and compare first value.
 *
 * @receiver [MutableSharedFlow]
 * @param T Type of channel
 * @param expectedValue Expected value
 * @param standardTestDispatcher Standard test dispatcher
 * @param testCoroutineScope Test coroutine scope
 * @return [Job]
 */
fun <T> Flow<T>.subscribeAndCompareFirstValueWithScope(
    expectedValue: T,
    standardTestDispatcher: TestDispatcher,
    testCoroutineScope: TestScope
): Job = this.take(1)
    .onEach {
        assertEquals(expectedValue, it)
    }.flowOn(standardTestDispatcher)
    .launchIn(testCoroutineScope)

/**
 * Subscribe and compare second value.
 *
 * @receiver [MutableSharedFlow]
 * @param T Type of channel
 * @param expectedValue Expected value
 * @param standardTestDispatcher Standard test dispatcher
 * @param testCoroutineScope Test coroutine scope
 * @return [Job]
 */
fun <T> Flow<T>.subscribeAndCompareSecondValue(
    expectedValue: T,
    standardTestDispatcher: TestDispatcher,
    testCoroutineScope: TestScope
): Job = this.drop(1).take(1)
    .onEach {
        assertEquals(expectedValue, it)
    }.flowOn(standardTestDispatcher)
    .launchIn(testCoroutineScope)

/**
 * Join job and wait until it will finish it throw an exception.
 *
 * @receiver [Job]
 * @param timeoutMillis Timeout millis
 */
suspend fun Job.joinWithTimeout(timeoutMillis: Long = 2 * ONE_SECOND) {
    withTimeoutOrNull(timeoutMillis) {
        join()
    } ?: throw TimeoutException("infinity job")
}

/**
 * Wait active subscription.
 *
 * @receiver Child of [SharedFlow]
 * @param R Type of flow parameter
 * @param T Type of flow
 * @param timeout Timeout
 * @return Child of [SharedFlow]
 */
fun <R, T : MutableSharedFlow<R>> T.waitActiveSubscription(timeout: Long = 2 * ONE_SECOND): T {
    val latch = CountDownLatch(1)
    onSubscription { latch.countDown() }
    if (subscriptionCount.value != 0)
        latch.countDown()
    if (!latch.await(timeout, TimeUnit.MILLISECONDS)) {
        if (subscriptionCount.value == 0)
            throw TimeoutException("Nobody didn't subscribe to this channel ${subscriptionCount.value}")
    }
    return this
}