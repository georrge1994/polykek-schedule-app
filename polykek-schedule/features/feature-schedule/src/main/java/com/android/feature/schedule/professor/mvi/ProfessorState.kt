package com.android.feature.schedule.professor.mvi

import com.android.core.ui.mvi.MviState

/**
 * Professor properties.
 */
internal interface ProfessorProperties {
    val weekTitle: String
    val lessonsAndHeaders: List<Any>
    val isLoading: Boolean
}

/**
 * Professor state.
 *
 * @constructor Create empty constructor for professor state
 */
internal sealed class ProfessorState : MviState, ProfessorProperties {
    /**
     * Default professor state.
     */
    internal object Default : ProfessorState() {
        override val weekTitle: String = ""
        override val lessonsAndHeaders: List<Any> = emptyList()
        override val isLoading: Boolean = false
    }

    /**
     * Update.
     *
     * @property weekTitle Week title
     * @property lessonsAndHeaders Lessons and titles
     * @property isLoading Is loading
     * @constructor Create [Update]
     */
    internal data class Update(
        override val weekTitle: String,
        override val lessonsAndHeaders: List<Any>,
        override val isLoading: Boolean
    ) : ProfessorState()

    /**
     * Copy state.
     *
     * @param weekTitle Week title
     * @param lessons Lessons
     * @param isLoading Is loading
     * @return [ProfessorState]
     */
    internal fun copyState(
        weekTitle: String = this.weekTitle,
        lessons: List<Any> = this.lessonsAndHeaders,
        isLoading: Boolean = this.isLoading
    ): ProfessorState = Update(weekTitle, lessons, isLoading)
}
