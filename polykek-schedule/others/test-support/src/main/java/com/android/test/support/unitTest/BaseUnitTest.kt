package com.android.test.support.unitTest

import org.junit.After
import org.junit.Before

const val ASSERT_DELTA = 0.001

/**
 * Base unit test.
 *
 * @constructor Create empty Base unit test
 */
abstract class BaseUnitTest {
    @Before
    open fun beforeTest() {

    }

    @After
    open fun afterTest() {}
}