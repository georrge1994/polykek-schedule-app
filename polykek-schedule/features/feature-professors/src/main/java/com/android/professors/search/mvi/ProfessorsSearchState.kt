package com.android.professors.search.mvi

import androidx.annotation.StringRes
import com.android.common.models.professors.Professor
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.mvi.MviState
import com.android.feature.professors.R

/**
 * Professors search properties.
 */
internal interface ProfessorsSearchProperties {
    val scheduleMode: ScheduleMode
    val professors: List<Professor>
    val isLoading: Boolean
    val messageId: Int
}

/**
 * Professors search state.
 *
 * @constructor Create empty constructor for professors search state
 */
internal sealed class ProfessorsSearchState : MviState, ProfessorsSearchProperties {
    /**
     * Default state.
     */
    internal data object Default : ProfessorsSearchState() {
        override val scheduleMode: ScheduleMode = ScheduleMode.SEARCH
        override val professors: List<Professor> = emptyList()

        @StringRes
        override val messageId: Int = R.string.professors_search_fragment_manual_text
        override val isLoading: Boolean = false
    }

    /**
     * Update.
     *
     * @property scheduleMode The mode of screen
     * @property professors The list of professors
     * @property isLoading The loading state
     * @constructor Create [Update]
     */
    internal data class Update(
        override val scheduleMode: ScheduleMode,
        override val professors: List<Professor>,
        override val isLoading: Boolean,
        @StringRes
        override val messageId: Int
    ) : ProfessorsSearchState()

    /**
     * Copy state.
     *
     * @param scheduleMode Schedule mode
     * @param professors Professors
     * @param isLoading Is loading
     * @return [ProfessorsSearchState]
     */
    internal fun copyState(
        scheduleMode: ScheduleMode = this.scheduleMode,
        professors: List<Professor> = this.professors,
        isLoading: Boolean = this.isLoading,
        messageId: Int = this.messageId
    ): ProfessorsSearchState = Update(
        scheduleMode = scheduleMode,
        professors = professors,
        isLoading = isLoading,
        messageId = messageId
    )
}
