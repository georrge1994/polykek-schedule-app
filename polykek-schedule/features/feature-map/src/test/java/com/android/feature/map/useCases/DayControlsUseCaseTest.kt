package com.android.feature.map.useCases

import com.android.feature.map.models.DayControls
import com.android.test.support.unitTest.BaseUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Day controls use case test for [DayControlsUseCase].
 *
 * @constructor Create empty constructor for day controls use case test
 */
class DayControlsUseCaseTest : BaseUnitTest() {
    private val dayControlsUseCase = DayControlsUseCase()

    /**
     * Left border.
     */
    @Test
    fun leftBorder() {
        assertEquals(
            DayControls(isPreviousEnabled = false, isNextEnabled = true, dayIndex = 0),
            dayControlsUseCase.getDayControls(0)
        )
    }

    /**
     * Middle position.
     */
    @Test
    fun middlePosition() {
        assertEquals(
            DayControls(isPreviousEnabled = true, isNextEnabled = true, dayIndex = 3),
            dayControlsUseCase.getDayControls(3)
        )
    }

    /**
     * Right border.
     */
    @Test
    fun rightBorder() {
        assertEquals(
            DayControls(isPreviousEnabled = true, isNextEnabled = false, dayIndex = 5),
            dayControlsUseCase.getDayControls(5)
        )
    }
}