package com.android.feature.schedule.base.viewModels

import com.android.feature.schedule.base.models.DatePickerInfo
import com.android.schedule.controller.api.IScheduleDateUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Base schedule view model test for [BaseScheduleViewModel].
 *
 * @constructor Create empty constructor for base schedule view model test
 */
class BaseScheduleViewModelTest : BaseViewModelUnitTest() {
    private val scheduleDateUseCase: IScheduleDateUseCase = mockk()
    private val baseScheduleViewModel = object : BaseScheduleViewModel(scheduleDateUseCase) {
        override suspend fun showNextWeek() {}
        override suspend fun showPreviousWeek() {}
        override suspend fun showSpecificDate(year: Int, month: Int, day: Int) {}
    }

    /**
     * Get date picker info for selected date.
     */
    @Test
    fun getDatePickerInfoForSelectedDate() {
        val date = Calendar.getInstance()
        coEvery { scheduleDateUseCase.selectedDate } returns date
        assertEquals(
            DatePickerInfo(day = date.get(Calendar.DATE), month = date.get(Calendar.MONTH), year = date.get(Calendar.YEAR)),
            baseScheduleViewModel.getDatePickerInfoForSelectedDate()
        )
    }
}