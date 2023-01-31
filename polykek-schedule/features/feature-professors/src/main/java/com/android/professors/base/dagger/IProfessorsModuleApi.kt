package com.android.professors.base.dagger

import androidx.fragment.app.Fragment
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Professors module api.
 */
interface IProfessorsModuleApi : IModuleApi {
    /**
     * Get professors fragment.
     *
     * @return [Fragment]
     */
    fun getProfessorsFragment(): Fragment

    /**
     * Get professor search fragment.
     *
     * @return [Fragment]
     */
    fun getProfessorSearchFragment(): Fragment
}