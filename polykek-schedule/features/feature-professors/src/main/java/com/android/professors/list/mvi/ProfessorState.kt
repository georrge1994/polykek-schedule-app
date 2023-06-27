package com.android.professors.list.mvi

import com.android.common.models.professors.Professor
import com.android.core.ui.mvi.MviState

/**
 * Professor state properties.
 */
internal interface ProfessorStateProperties {
    val professors: List<Professor>
}

/**
 * Professor state.
 *
 * @constructor Create empty constructor for professor state
 */
internal sealed class ProfessorState : MviState, ProfessorStateProperties {
    /**
     * Default professor state.
     */
    internal object Default : ProfessorState() {
        override val professors: List<Professor> = emptyList()
    }

    /**
     * Update.
     *
     * @property professors Professors
     * @constructor Create [Update]
     */
    internal data class Update(override val professors: List<Professor>) : ProfessorState()

    /**
     * Copy state.
     *
     * @param professors Professors
     * @return [ProfessorState]
     */
    internal fun copyState(
        professors: List<Professor> = this.professors
    ): ProfessorState = Update(professors)
}
