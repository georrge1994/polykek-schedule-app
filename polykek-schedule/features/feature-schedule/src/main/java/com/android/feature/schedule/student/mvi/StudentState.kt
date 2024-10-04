package com.android.feature.schedule.student.mvi

import com.android.common.models.schedule.Day
import com.android.common.models.schedule.stubWeek
import com.android.core.ui.mvi.MviState

/**
 * Student properties.
 */
internal interface StudentProperties {
    val weekTitle: String
    val dayTitle: String
    val days: Map<Int, Day>
    val isLoading: Boolean
    val viewPagerPosition: Int
    val smoothPaging: Boolean
}

/**
 * Student state.
 *
 * @constructor Create empty constructor for student state
 */
internal sealed class StudentState : MviState, StudentProperties {
    /**
     * Default student state.
     */
    internal data object Default : StudentState() {
        override val weekTitle: String = stubWeek.title
        override val dayTitle: String = ""
        override val days: Map<Int, Day> = stubWeek.days
        override val isLoading: Boolean = true
        override val viewPagerPosition: Int = 0
        override val smoothPaging: Boolean = false
    }

    /**
     * Update.
     *
     * @property weekTitle Week title
     * @property dayTitle Day title
     * @property days Days
     * @property isLoading Is loading
     * @property viewPagerPosition View pager position
     * @property smoothPaging Smooth paging of view pager
     * @constructor Create [Update]
     */
    internal data class Update(
        override val weekTitle: String,
        override val dayTitle: String,
        override val days: Map<Int, Day>,
        override val isLoading: Boolean,
        override val viewPagerPosition: Int,
        override val smoothPaging: Boolean = false
    ) : StudentState()

    /**
     * Copy state.
     *
     * @param weekTitle Week title
     * @param dayTitle Day title
     * @param days Days
     * @param isLoading Is loading
     * @param viewPagerPosition View pager position
     * @param smoothPaging Smooth paging
     * @return [StudentState]
     */
    internal fun copyState(
        weekTitle: String = this.weekTitle,
        dayTitle: String = this.dayTitle,
        days: Map<Int, Day> = this.days,
        isLoading: Boolean = this.isLoading,
        viewPagerPosition: Int = this.viewPagerPosition,
        smoothPaging: Boolean = this.smoothPaging
    ): StudentState = Update(
        weekTitle = weekTitle,
        dayTitle = dayTitle,
        days = days,
        isLoading = isLoading,
        viewPagerPosition = viewPagerPosition,
        smoothPaging = smoothPaging
    )
}
