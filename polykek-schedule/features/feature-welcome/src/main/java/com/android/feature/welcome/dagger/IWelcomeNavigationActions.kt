package com.android.feature.welcome.dagger

import androidx.fragment.app.Fragment

/**
 * Welcome inner navigation.
 */
interface IWelcomeNavigationActions {
    /**
     * Get schools.
     *
     * @return [Fragment]
     */
    fun getSchools(): Fragment

    /**
     * Get professor search.
     *
     * @return [Fragment]
     */
    fun getProfessorSearch(): Fragment
}