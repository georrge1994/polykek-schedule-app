package com.android.feature.main.screen.dagger

import androidx.fragment.app.Fragment

/**
 * Main screen navigation actions.
 */
interface IMainScreenNavigationActions {
    /**
     * Get schedule fragment.
     *
     * @return [Fragment]
     */
    fun getScheduleFragment(): Fragment

    /**
     * Get notes fragment.
     *
     * @return [Fragment]
     */
    fun getNotesFragment(): Fragment

    /**
     * Get map fragment.
     *
     * @return [Fragment]
     */
    fun getMapFragment(): Fragment

    /**
     * Get professor fragment.
     *
     * @return [Fragment]
     */
    fun getProfessorFragment(): Fragment

    /**
     * Get schools fragment.
     *
     * @return [Fragment]
     */
    fun getSchoolsFragment(): Fragment

    /**
     * Get professor search fragment.
     *
     * @return [Fragment]
     */
    fun getProfessorSearchFragment(): Fragment
}