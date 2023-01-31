package com.android.test.support.testFixtures

import io.mockk.MockKVerificationScope
import kotlinx.coroutines.runBlocking


/**
 * Run blocking unit
 *
 * @param action
 */
fun runBlockingUnit(action: suspend () -> Unit) {
    runBlocking {
        action.invoke()
    }
}

/**
 * One of.
 *
 * @receiver [MockKVerificationScope]
 * @param T Type
 * @param items Items
 * @return Capture function for input arguments
 */
inline fun <reified T : Any> MockKVerificationScope.oneOf(vararg items: T) =
    capture(items.toMutableList())