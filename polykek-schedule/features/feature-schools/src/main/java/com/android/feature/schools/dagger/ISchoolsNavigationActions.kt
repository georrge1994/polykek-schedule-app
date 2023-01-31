package com.android.feature.schools.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.models.ScheduleMode

/**
 * Schools navigation actions.
 */
interface ISchoolsNavigationActions {
    /**
     * Get groups screen.
     *
     * @param scheduleMode [ScheduleMode]
     * @param schoolId School id
     * @param abbr Abbr
     * @return [Fragment]
     */
    fun getGroupsScreen(scheduleMode: ScheduleMode, schoolId: String, abbr: String): Fragment
}