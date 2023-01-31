package com.android.schedule.controller.impl.useCases

import com.android.test.support.unitTest.BaseUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Schedule date use case test for [ScheduleDateUseCase].
 *
 * @constructor Create empty constructor for schedule date use case test
 */
class ScheduleDateUseCaseTest : BaseUnitTest() {
    private val scheduleDateUseCase = ScheduleDateUseCase()

    /**
     * Complex test.
     */
    @Test
    fun complexTest() {
        scheduleDateUseCase.setSelectedDay(year = 2022, month = 1, day = 1)
        assertEquals("2022-02-01", scheduleDateUseCase.getPeriod())
        scheduleDateUseCase.addToSelectedDate(Calendar.DATE, 1)
        assertEquals("2022-02-02", scheduleDateUseCase.getPeriod())
    }
}