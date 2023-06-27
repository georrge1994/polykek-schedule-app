package com.android.feature.schedule.professor.mvi

import com.android.core.ui.mvi.MviIntent

/**
 * Professor intent.
 *
 * @constructor Create empty constructor for professor intent
 */
internal sealed class ProfessorIntent : MviIntent {
    /**
     * Show next week.
     */
    internal object ShowNextWeek : ProfessorIntent()

    /**
     * Show previous week.
     */
    internal object ShowPreviousWeek : ProfessorIntent()

    /**
     * Show specific date.
     *
     * @property year Year
     * @property month Month
     * @property day Day
     * @constructor Create [ShowSpecificDate]
     */
    internal data class ShowSpecificDate(
        val year: Int,
        val month: Int,
        val day: Int
    ) : ProfessorIntent()

    /**
     * Update professor id.
     *
     * @property professorId Selected professor id
     * @constructor Create [UpdateProfessorId]
     */
    internal data class UpdateProfessorId(val professorId: Int?) : ProfessorIntent()

    /**
     * Check period and refresh.
     */
    internal object CheckPeriodAndRefresh : ProfessorIntent()

    /**
     * Show data picker.
     */
    internal object ShowDataPicker : ProfessorIntent()
}