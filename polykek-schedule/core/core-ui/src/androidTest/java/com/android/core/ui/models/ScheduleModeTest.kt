package com.android.core.ui.models

import com.android.test.support.androidTest.BaseAndroidUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Schedule mode test.
 *
 * @constructor Create empty constructor for schedule mode test
 */
class ScheduleModeTest : BaseAndroidUnitTest() {
    /**
     * Test for schedule shortcut-functions.
     */
    @Test
    fun getScheduleModeBundleTest() {
        val bundle = getScheduleModeBundle(ScheduleMode.WELCOME)
        assertEquals(ScheduleMode.WELCOME.ordinal, bundle.getInt("SCHEDULE_MODE"))
        assertEquals(ScheduleMode.WELCOME, bundle.getScheduleMode())
    }
}