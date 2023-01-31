package com.android.professors.base.dagger

import androidx.fragment.app.Fragment

/**
 * Schools navigation actions.
 */
interface IProfessorsNavigationActions {
    /**
     * Get main screen.
     *
     * @return [Fragment]
     */
    fun getMainScreen(): Fragment

    /**
     * Get professor schedule fragment.
     *
     * @param id Professor id
     * @param title Professor title
     * @return [Fragment]
     */
    fun getProfessorScheduleFragment(id: Int, title: String?): Fragment

    /**
     * Get faq fragment.
     *
     * @return [Fragment]
     */
    fun getFaqFragment(): Fragment
}