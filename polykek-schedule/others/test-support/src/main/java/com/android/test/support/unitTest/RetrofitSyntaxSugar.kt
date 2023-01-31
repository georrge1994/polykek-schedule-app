package com.android.test.support.unitTest

import com.android.common.models.api.Resource
import org.junit.Assert.assertEquals

/**
 * Check negative result.
 *
 * @param T Type of response
 * @receiver [Resource]
 */
fun <T> Resource<T>.checkNegativeResult() {
    assertEquals("Client Error", message)
    assertEquals(null, data)
}