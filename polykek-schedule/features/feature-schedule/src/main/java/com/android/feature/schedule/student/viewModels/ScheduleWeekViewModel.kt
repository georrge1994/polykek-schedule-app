package com.android.feature.schedule.student.viewModels

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.android.common.models.schedule.Week
import com.android.common.models.schedule.stubWeek
import com.android.feature.schedule.R
import com.android.feature.schedule.base.viewModels.BaseScheduleViewModel
import com.android.feature.schedule.base.viewModels.ONE_WEEK
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.IScheduleDateUseCase
import com.android.shared.code.utils.general.toCalendar
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val CURRENT_DAY_TITLE = "dd.MM.yyyy"

/**
 * Schedule week view model provides logic for first tab - "student" schedule tab.
 *
 * @property application Application object to get context
 * @property scheduleController Schedule controller allows to synchronize UI changes between all tabs
 * @property scheduleDateUseCase Use case to work with schedule dates
 * @constructor Create [ScheduleWeekViewModel]
 */
internal class ScheduleWeekViewModel @Inject constructor(
    private val application: Application,
    private val scheduleController: IScheduleController,
    private val scheduleDateUseCase: IScheduleDateUseCase
) : BaseScheduleViewModel(scheduleDateUseCase) {
    private val titleFormat = SimpleDateFormat(CURRENT_DAY_TITLE, Locale.ENGLISH)
    val viewPagerPosition = MutableLiveData<Pair<Int, Boolean>>()
    val weekTitle = Transformations.map(schedule) { week ->
        week?.title ?: application.getString(R.string.schedule_fragment_week)
    }
    val dayTitleLiveData = MediatorLiveData<String>().apply {
        addSource(schedule) {
            postValue(getScheduleTitle())
        }
    }

    init {
        scheduleController.indexOfDay = getViewPagerPositionFromDate()
    }

    override suspend fun subscribe() {
        super.subscribe()
        viewPagerPosition.postValue(Pair(scheduleController.indexOfDay, false))
        isLoading.postValue(true)
        subscribeToWeekFlow()
    }

    /**
     * Subscribe to week flow.
     */
    private fun subscribeToWeekFlow() = scheduleController.weekFlow
        .onEach { week ->
            schedule.postValue(week)
            if (week != stubWeek)
                isLoading.postValue(false)
        }.cancelableLaunchInBackground()

    /**
     * Get lesson's live data by days.
     *
     * @param dayId Day id
     */
    internal fun getLessonsLiveData(dayId: Int) = Transformations.map(schedule) { week ->
        week?.days?.get(dayId)?.lessons ?: Collections.emptyList()
    }

    /**
     * Swipe day within one week.
     *
     * @param position Position
     */
    internal fun swipeDay(position: Int) = viewModelScope.launch {
        scheduleDateUseCase.addToSelectedDate(Calendar.DATE, position - scheduleController.indexOfDay)
        scheduleController.indexOfDay = position
        dayTitleLiveData.postValue(getScheduleTitle())
    }

    override suspend fun showSpecificDate(year: Int, month: Int, day: Int) {
        scheduleDateUseCase.setSelectedDay(year, month, day)
        scheduleController.indexOfDay = getViewPagerPositionFromDate(force = true)
        viewPagerPosition.postValue(Pair(scheduleController.indexOfDay, true))
        scheduleController.updateSchedule(scheduleDateUseCase.getPeriod())
    }

    override suspend fun showNextWeek() = shiftWeek(ONE_WEEK)

    override suspend fun showPreviousWeek() = shiftWeek(-ONE_WEEK)

    /**
     * Only the current week should shows the current day. All other weeks will show Monday. Additionally we have the 1st
     * September feature and datePicker (it should shows the selected day). So, we need to keep all these points when we
     * calculate selected date.
     *
     * @param weekNumber Count of week
     */
    private suspend fun shiftWeek(weekNumber: Int) {
        // save old state of viewpager
        val positionInPreviousWeek = scheduleController.indexOfDay
        // shift current week +-7 days
        scheduleDateUseCase.addToSelectedDate(Calendar.WEEK_OF_YEAR, weekNumber)
        // get desired viewpager position
        scheduleController.indexOfDay = getViewPagerPositionFromDate()
        // shift selected date taking into account an old and new viewpager positions
        scheduleDateUseCase.addToSelectedDate(Calendar.DATE, scheduleController.indexOfDay - positionInPreviousWeek)
        viewPagerPosition.postValue(Pair(scheduleController.indexOfDay, true))
        scheduleController.updateSchedule(scheduleDateUseCase.getPeriod())
    }

    /**
     * Get title for schedule screen.
     *
     * @return Date in the specific format
     */
    private fun getScheduleTitle(): String = titleFormat.format(scheduleDateUseCase.selectedDate.time)

    /**
     * If week is a current week, will show the current day. Otherwise will show the Monday. Also use can force logic for data-picker.
     *
     * @param force Force flag to ignore logic
     * @return ViewPager position or index of day in [Week]
     */
    private fun getViewPagerPositionFromDate(force: Boolean = false): Int {
        val currentWeekCalInst = scheduleDateUseCase.getCurrentDay().toCalendar()
        // Calendar.DAY_OF_WEEK in 1..7, but viewpager2 is needed in 0..6, so -1.
        // The first day of Calendar is Sunday, but normal week starts from Monday, so the second -1.
        return when {
            force -> scheduleDateUseCase.selectedDate.get(Calendar.DAY_OF_WEEK) - 2
            currentWeekCalInst.get(Calendar.WEEK_OF_YEAR) == scheduleDateUseCase.selectedDate.get(Calendar.WEEK_OF_YEAR) ->
                currentWeekCalInst.get(Calendar.DAY_OF_WEEK) - 2
            else -> 0
        }
    }
}