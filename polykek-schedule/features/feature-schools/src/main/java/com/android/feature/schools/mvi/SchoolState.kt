package com.android.feature.schools.mvi

import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.mvi.MviState
import com.android.feature.schools.models.School

/**
 * School state properties.
 */
internal interface SchoolStateProperties {
    val schools: List<School>?
    val scheduleMode: ScheduleMode
    val isLoading: Boolean
}

/**
 * School state.
 *
 * @constructor Create empty constructor for school state
 */
internal sealed class SchoolState : MviState, SchoolStateProperties {
    /**
     * Default state.
     */
    internal data object Default : SchoolState() {
        override val schools: List<School> = emptyList()
        override val scheduleMode: ScheduleMode = ScheduleMode.SEARCH
        override val isLoading: Boolean = false
    }

    /**
     * Update.
     *
     * @property schools Schools
     * @property scheduleMode Schedule mode
     * @property isLoading Is loading
     * @constructor Create [Update]
     */
    internal data class Update(
        override val schools: List<School>?,
        override val scheduleMode: ScheduleMode,
        override val isLoading: Boolean
    ) : SchoolState()

    /**
     * Copy state.
     *
     * @param schools Schools
     * @param scheduleMode Schedule mode
     * @param isLoading Is loading
     * @return [SchoolState]
     */
    internal fun copyState(
        schools: List<School>? = this.schools,
        scheduleMode: ScheduleMode = this.scheduleMode,
        isLoading: Boolean = this.isLoading
    ): SchoolState = Update(schools, scheduleMode, isLoading)
}