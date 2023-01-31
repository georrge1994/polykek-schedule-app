package com.android.feature.groups.dagger

import androidx.fragment.app.Fragment
import com.android.core.ui.models.ScheduleMode
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Groups api module.
 */
interface IGroupsModuleApi : IModuleApi {
    /**
     * Get groups screen.
     *
     * @return [Fragment]
     */
    fun getGroupsFragment(): Fragment

    /**
     * Get groups fragment.
     *
     * @param scheduleMode Schedule mode
     * @param schoolId School id
     * @param abbr Abbr
     * @return [Fragment]
     */
    fun getGroupsFragment(scheduleMode: ScheduleMode, schoolId: String, abbr: String): Fragment
}