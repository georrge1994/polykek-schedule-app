package com.android.feature.schedule.student.mvi

import com.android.core.ui.mvi.MviAction

/**
 * Student action.
 *
 * @constructor Create empty constructor for student action
 */
internal sealed class StudentAction : MviAction {
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
    ) : StudentAction()

    /**
     * Open note editor.
     *
     * @property dayId Day id
     * @property noteId Note id
     * @property title Note title
     * @constructor Create [OpenNoteEditor]
     */
    internal data class OpenNoteEditor(
        val dayId: Int,
        val noteId: String,
        val title: String
    ) : StudentAction()
}
