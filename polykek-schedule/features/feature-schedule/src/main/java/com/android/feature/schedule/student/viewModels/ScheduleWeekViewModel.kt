package com.android.feature.schedule.student.viewModels

import android.app.Application
import com.android.common.models.schedule.Week
import com.android.common.models.schedule.stubWeek
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.feature.schedule.R
import com.android.feature.schedule.student.mvi.StudentAction
import com.android.feature.schedule.student.mvi.StudentIntent
import com.android.feature.schedule.student.mvi.StudentState
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.IScheduleDateUseCase
import com.android.shared.code.utils.general.toCalendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

private const val CURRENT_DAY_TITLE = "dd.MM.yyyy"
private const val ONE_WEEK = 1

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
) : BaseSubscriptionViewModel<StudentIntent, StudentState, StudentAction>(StudentState.Default) {
    private val titleFormat = SimpleDateFormat(CURRENT_DAY_TITLE, Locale.ENGLISH)

    init {
        scheduleController.indexOfDay = getViewPagerPositionFromDate()
    }

    override suspend fun subscribe() {
        super.subscribe()
        currentState.copyState(
            viewPagerPosition = scheduleController.indexOfDay,
            smoothPaging = false
        ).emitState()
        // Subscribe to week flow
        scheduleController.weekFlow.onEach { week ->
            if (week != stubWeek) {
                currentState.copyState(
                    weekTitle = week?.title ?: application.getString(R.string.schedule_fragment_week),
                    dayTitle = getScheduleTitle(),
                    days = week?.days ?: emptyMap(),
                    isLoading = false
                ).emitState()
            }
        }.cancelableLaunchInBackground()
    }

    override suspend fun dispatchIntent(intent: StudentIntent) {
        when (intent) {
            StudentIntent.ShowDataPicker -> openDatePickerForSelectedDate()
            StudentIntent.ShowNextWeek -> shiftWeek(ONE_WEEK)
            StudentIntent.ShowPreviousWeek -> shiftWeek(-ONE_WEEK)
            is StudentIntent.ShowSpecificDate -> showSpecificDate(intent.year, intent.month, intent.day)
            is StudentIntent.SwipeDay -> swipeDay(intent.position)
            is StudentIntent.OpenNoteEditor ->
                StudentAction.OpenNoteEditor(intent.dayId, intent.noteId, intent.title).emitAction()
        }
    }

    /**
     * Swipe day within one week.
     *
     * @param position Position
     */
    private suspend fun swipeDay(position: Int) = withContext(Dispatchers.Default) {
        scheduleDateUseCase.addToSelectedDate(Calendar.DATE, position - scheduleController.indexOfDay)
        scheduleController.indexOfDay = position
        currentState.copyState(
            dayTitle = getScheduleTitle(),
            viewPagerPosition = scheduleController.indexOfDay,
            smoothPaging = false
        ).emitState()
    }

    /**
     * Show specific date.
     *
     * @param year Year
     * @param month Month
     * @param day Day
     */
    private suspend fun showSpecificDate(year: Int, month: Int, day: Int) = withContext(Dispatchers.Default) {
        scheduleDateUseCase.setSelectedDay(year, month, day)
        scheduleController.indexOfDay = getViewPagerPositionFromDate(force = true)
        currentState.copyState(
            isLoading = true,
            viewPagerPosition = scheduleController.indexOfDay,
            smoothPaging = true
        ).emitState()
        scheduleController.updateSchedule(scheduleDateUseCase.getPeriod())
    }

    /**
     * Only the current week should shows the current day. All other weeks will show Monday. Additionally we have the
     * 1st September feature and datePicker (it should shows the selected day). So, we need to keep all these points
     * when we calculate selected date.
     *
     * @param weekNumber Count of week
     */
    private suspend fun shiftWeek(weekNumber: Int) {
        // Save the old viewpager position.
        val positionInPreviousWeek = scheduleController.indexOfDay
        // Shift current week +-7 days.
        scheduleDateUseCase.addToSelectedDate(Calendar.WEEK_OF_YEAR, weekNumber)
        // Get the desired viewpager position.
        scheduleController.indexOfDay = getViewPagerPositionFromDate()
        // Shift selected date taking into account the old and new viewpager positions.
        scheduleDateUseCase.addToSelectedDate(
            Calendar.DATE,
            scheduleController.indexOfDay - positionInPreviousWeek
        )
        currentState.copyState(
            isLoading = true,
            viewPagerPosition = scheduleController.indexOfDay,
            smoothPaging = true
        ).emitState()
        scheduleController.updateSchedule(scheduleDateUseCase.getPeriod())
    }

    /**
     * Get title for schedule screen.
     *
     * @return Date in the specific format
     */
    private fun getScheduleTitle(): String = titleFormat.format(scheduleDateUseCase.selectedDate.time)

    /**
     * If week is a current week, will show the current day. Otherwise will show the Monday. Also use can force logic
     * for data-picker.
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

    /**
     * Open date picker for selected date.
     */
    private suspend fun openDatePickerForSelectedDate() = with(scheduleDateUseCase.selectedDate) {
        StudentAction.OpenDatePicker(
            day = get(Calendar.DAY_OF_MONTH),
            month = get(Calendar.MONTH),
            year = get(Calendar.YEAR)
        ).emitAction()
    }
}