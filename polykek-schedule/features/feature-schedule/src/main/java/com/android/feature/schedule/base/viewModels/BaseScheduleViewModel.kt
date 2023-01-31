package com.android.feature.schedule.base.viewModels

import androidx.lifecycle.MutableLiveData
import com.android.common.models.schedule.Week
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.feature.schedule.base.models.DatePickerInfo
import com.android.schedule.controller.api.IScheduleDateUseCase
import java.util.*

internal const val ONE_WEEK = 1

/**
 * Base schedule view model for student and professor screens.
 *
 * @property scheduleDateUseCase Use case to work with schedule dates
 * @constructor Create [BaseScheduleViewModel]
 */
internal abstract class BaseScheduleViewModel(private val scheduleDateUseCase: IScheduleDateUseCase) : BaseSubscriptionViewModel() {
    protected val schedule = MutableLiveData<Week?>()

    /**
     * Show the next week async.
     */
    internal fun showNextWeekAsync() = executeWithLoadingAnimation { showNextWeek() }

    /**
     * Show the previous week async.
     */
    internal fun showPreviousWeekAsync() = executeWithLoadingAnimation { showPreviousWeek() }

    /**
     * Show the specific date async.
     *
     * @param year Year
     * @param month Month
     * @param day Day
     */
    internal fun showSpecificDateAsync(year: Int, month: Int, day: Int) = executeWithLoadingAnimation { showSpecificDate(year, month, day) }

    /**
     * Show the next week.
     */
    protected abstract suspend fun showNextWeek()

    /**
     * Show the previous week.
     */
    protected abstract suspend fun showPreviousWeek()

    /**
     * Show the specific date.
     *
     * @param year Year
     * @param month Month
     * @param day Day
     */
    protected abstract suspend fun showSpecificDate(year: Int, month: Int, day: Int)

    /**
     * Get date picker info for selected date.
     *
     * @return [DatePickerInfo]
     */
    internal fun getDatePickerInfoForSelectedDate(): DatePickerInfo = with(scheduleDateUseCase.selectedDate) {
        DatePickerInfo(
            day = get(Calendar.DAY_OF_MONTH),
            month = get(Calendar.MONTH),
            year = get(Calendar.YEAR)
        )
    }
}