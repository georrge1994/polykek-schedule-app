package com.android.feature.schedule.base.dagger

import androidx.fragment.app.Fragment
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Schedule module api.
 */
interface IScheduleModuleApi : IModuleApi {
    /**
     * Get professor schedule fragment.
     *
     * @param id Id
     * @param title Title
     * @return [Fragment]
     */
    fun getProfessorScheduleFragment(id: Int, title: String?): Fragment

    /**
     * Get schedule week fragment.
     *
     * @return [Fragment]
     */
    fun getScheduleWeekFragment(): Fragment
}