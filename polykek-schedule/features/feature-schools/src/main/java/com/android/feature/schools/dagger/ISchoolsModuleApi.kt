package com.android.feature.schools.dagger

import androidx.fragment.app.Fragment
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Schools api module.
 */
interface ISchoolsModuleApi : IModuleApi {
    /**
     * Get schools screen.
     *
     * @return [Fragment]
     */
    fun getSchoolsFragment(): Fragment
}