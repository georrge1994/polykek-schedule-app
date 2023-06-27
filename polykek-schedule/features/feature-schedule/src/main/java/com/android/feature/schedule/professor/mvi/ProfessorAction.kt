package com.android.feature.schedule.professor.mvi

import com.android.core.ui.mvi.MviAction

/**
 * Professor action.
 *
 * @constructor Create empty constructor for professor action
 */
internal sealed class ProfessorAction : MviAction {
    /**
     * Open date picker.
     *
     * @property day Day
     * @property month Month
     * @property year Year
     * @constructor Create [OpenDatePicker]
     */
    internal data class OpenDatePicker(
        val day: Int,
        val month: Int,
        val year: Int
    ) : ProfessorAction()
}