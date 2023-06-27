package com.android.feature.schedule.student.mvi

import com.android.core.ui.mvi.MviIntent

/**
 * Student intent.
 *
 * @constructor Create empty constructor for student intent
 */
internal sealed class StudentIntent : MviIntent {
    /**
     * Swipe day.
     *
     * @property position Position
     * @constructor Create [SwipeDay]
     */
    internal data class SwipeDay(val position: Int) : StudentIntent()

    /**
     * Show next week.
     */
    internal object ShowNextWeek : StudentIntent()

    /**
     * Show previous week.
     */
    internal object ShowPreviousWeek : StudentIntent()

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
    ) : StudentIntent()

    /**
     * Show data picker.
     */
    internal object ShowDataPicker : StudentIntent()

    /**
     * Open note editor.
     *
     * @property dayId Day id
     * @property noteId Note id
     * @property title Title
     * @constructor Create [OpenNoteEditor]
     */
    internal data class OpenNoteEditor(
        val dayId: Int,
        val noteId: String,
        val title: String
    ) : StudentIntent()
}