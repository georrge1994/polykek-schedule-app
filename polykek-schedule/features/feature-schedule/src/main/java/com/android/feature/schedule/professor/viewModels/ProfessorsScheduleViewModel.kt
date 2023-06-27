package com.android.feature.schedule.professor.viewModels

import android.app.Application
import com.android.core.ui.mvi.MviViewModel
import com.android.feature.schedule.R
import com.android.feature.schedule.professor.mvi.ProfessorAction
import com.android.feature.schedule.professor.mvi.ProfessorIntent
import com.android.feature.schedule.professor.mvi.ProfessorState
import com.android.feature.schedule.professor.useCases.ProfessorScheduleUseCase
import com.android.schedule.controller.api.IScheduleDateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

private const val ONE_WEEK = 1

/**
 * Provides logic for "professor" schedule screen (4th tab).
 *
 * @property application Application object to get context
 * @property professorScheduleUseCase Professor schedule use case
 * @property scheduleDateUseCase Use case to work with schedule dates
 * @constructor Create [ProfessorsScheduleViewModel]
 */
internal class ProfessorsScheduleViewModel @Inject constructor(
    private val application: Application,
    private val professorScheduleUseCase: ProfessorScheduleUseCase,
    private val scheduleDateUseCase: IScheduleDateUseCase
) : MviViewModel<ProfessorIntent, ProfessorState, ProfessorAction>(ProfessorState.Default) {
    private var lastRequestPeriod: String? = null
    private var professorId: Int? = null

    override suspend fun dispatchIntent(intent: ProfessorIntent) {
        when (intent) {
            ProfessorIntent.CheckPeriodAndRefresh -> checkAndRefresh()
            ProfessorIntent.ShowDataPicker -> openDatePickerForSelectedDate()
            ProfessorIntent.ShowNextWeek -> showNextWeek()
            ProfessorIntent.ShowPreviousWeek -> showPreviousWeek()
            is ProfessorIntent.ShowSpecificDate -> showSpecificDate(intent.year, intent.month, intent.day)
            is ProfessorIntent.UpdateProfessorId -> updateProfessorSchedule(professorId = intent.professorId)
        }
    }

    /**
     * App has the one period for both "student" and "professor" schedule screens. When user changes the period on the
     * student screen, we also have to update schedule on the professor screen.
     */
    private suspend fun checkAndRefresh() = withContext(Dispatchers.Default) {
        if (lastRequestPeriod != scheduleDateUseCase.getPeriod()) {
            updateProfessorSchedule(professorId)
        }
    }

    /**
     * Show next week for selected professor.
     */
    private suspend fun showNextWeek() = withContext(Dispatchers.Default) {
        scheduleDateUseCase.addToSelectedDate(Calendar.WEEK_OF_YEAR, ONE_WEEK)
        updateProfessorSchedule(professorId)
    }

    /**
     * Show previous week for selected professor.
     */
    private suspend fun showPreviousWeek() = withContext(Dispatchers.Default) {
        scheduleDateUseCase.addToSelectedDate(Calendar.WEEK_OF_YEAR, -ONE_WEEK)
        updateProfessorSchedule(professorId)
    }

    /**
     * Show specific date.
     *
     * @param year Year
     * @param month Month
     * @param day Day
     * @return [Unit]
     */
    private suspend fun showSpecificDate(year: Int, month: Int, day: Int) = withContext(Dispatchers.Default) {
        scheduleDateUseCase.setSelectedDay(year, month, day)
        updateProfessorSchedule(professorId)
    }

    /**
     * Update professor schedule.
     *
     * @param professorId Professor id
     */
    private suspend fun updateProfessorSchedule(professorId: Int?) = withContext(Dispatchers.IO) {
        professorId ?: return@withContext
        this@ProfessorsScheduleViewModel.professorId = professorId
        currentState.copyState(isLoading = true).emitState()
        val period = scheduleDateUseCase.getPeriod()
        val week = professorScheduleUseCase.getProfessorSchedule(professorId, period)
        currentState.copyState(
            weekTitle = week?.title ?: application.getString(R.string.schedule_fragment_week),
            lessons = professorScheduleUseCase.getRecyclerItems(week),
            isLoading = false,
        ).emitState()
        lastRequestPeriod = period
    }

    /**
     * Open date picker for selected date.
     */
    private suspend fun openDatePickerForSelectedDate() = with(scheduleDateUseCase.selectedDate) {
        ProfessorAction.OpenDatePicker(
            day = get(Calendar.DAY_OF_MONTH),
            month = get(Calendar.MONTH),
            year = get(Calendar.YEAR)
        ).emitAction()
    }
}