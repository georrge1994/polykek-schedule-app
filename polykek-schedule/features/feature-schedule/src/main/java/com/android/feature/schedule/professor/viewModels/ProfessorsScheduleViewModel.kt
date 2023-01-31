package com.android.feature.schedule.professor.viewModels

import android.app.Application
import androidx.lifecycle.Transformations
import com.android.feature.schedule.R
import com.android.feature.schedule.base.viewModels.BaseScheduleViewModel
import com.android.feature.schedule.base.viewModels.ONE_WEEK
import com.android.feature.schedule.professor.useCases.ProfessorScheduleUseCase
import com.android.schedule.controller.api.IScheduleDateUseCase
import com.android.shared.code.utils.syntaxSugar.postValueIfChanged
import java.util.*
import javax.inject.Inject

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
) : BaseScheduleViewModel(scheduleDateUseCase) {
    private var lastRequestPeriod: String? = null
    private var professorId: Int? = null

    val weekTitle = Transformations.map(schedule) { week ->
        week?.title ?: application.getString(R.string.schedule_fragment_week)
    }
    val lessons = Transformations.map(schedule) { week ->
        professorScheduleUseCase.getRecyclerItems(week)
    }
    val listIsEmpty = Transformations.map(lessons) { week ->
        week.isNullOrEmpty()
    }

    override suspend fun showNextWeek() {
        scheduleDateUseCase.addToSelectedDate(Calendar.WEEK_OF_YEAR, ONE_WEEK)
        updateProfessorSchedule(professorId)
    }

    override suspend fun showPreviousWeek() {
        scheduleDateUseCase.addToSelectedDate(Calendar.WEEK_OF_YEAR, -ONE_WEEK)
        updateProfessorSchedule(professorId)
    }

    override suspend fun showSpecificDate(year: Int, month: Int, day: Int) {
        scheduleDateUseCase.setSelectedDay(year, month, day)
        updateProfessorSchedule(professorId)
    }

    /**
     * Update professor schedule.
     *
     * @param professorId Professor id
     */
    internal fun updateProfessorSchedule(professorId: Int?) = executeWithLoadingAnimation {
        professorId ?: return@executeWithLoadingAnimation
        this.professorId = professorId
        scheduleDateUseCase.getPeriod().let { period ->
            lastRequestPeriod = period
            schedule.postValueIfChanged(professorScheduleUseCase.getProfessorSchedule(professorId, period))
        }
    }

    /**
     * App has the one period for both "student" and "professor" schedule screens. When user changes the period on the student screen,
     * we also have to update schedule on the professor screen. It's not necessary vice verde, because student screen has
     * schedule-controller. This is a hotfix.
     */
    internal fun checkAndRefresh() = launchInBackground {
        if (lastRequestPeriod != scheduleDateUseCase.getPeriod()) {
            updateProfessorSchedule(professorId)
        }
    }
}