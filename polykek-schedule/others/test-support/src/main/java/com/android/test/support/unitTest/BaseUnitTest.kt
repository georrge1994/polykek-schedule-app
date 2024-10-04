package com.android.test.support.unitTest

import org.junit.After
import org.junit.Before

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