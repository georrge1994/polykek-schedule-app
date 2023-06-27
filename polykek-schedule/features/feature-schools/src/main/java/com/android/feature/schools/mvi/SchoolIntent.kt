package com.android.feature.schools.mvi

import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.mvi.MviIntent
import com.android.feature.schools.models.School

/**
 * School intent.
 *
 * @constructor Create empty constructor for school intent
 */
internal sealed class SchoolIntent : MviIntent {
    /**
     * Init content.
     *
     * @property scheduleMode Schedule mode
     * @constructor Create [InitContent]
     */
    internal data class InitContent(val scheduleMode: ScheduleMode) : SchoolIntent()

    /**
     * Show groups.
     *
     * @property school School
     * @constructor Create [ShowGroups]
     */
    internal data class ShowGroups(val school: School) : SchoolIntent()
}