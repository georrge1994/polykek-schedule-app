package com.android.feature.schools.mvi

import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.mvi.MviAction
import com.android.feature.schools.models.School

/**
 * School action.
 *
 * @constructor Create empty constructor for school action
 */
internal sealed class SchoolAction : MviAction {
    /**
     * Show groups.
     *
     * @property school School
     * @property scheduleMode Schedule mode
     * @constructor Create [ShowGroups]
     */
    internal data class ShowGroups(val school: School, val scheduleMode: ScheduleMode) : SchoolAction()
}