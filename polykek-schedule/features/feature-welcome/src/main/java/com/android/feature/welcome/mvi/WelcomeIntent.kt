package com.android.feature.welcome.mvi

import com.android.core.ui.mvi.MviIntent

/**
 * Welcome intent.
 *
 * @constructor Create empty constructor for welcome intent
 */
internal sealed class WelcomeIntent : MviIntent {
    /**
     * Change position.
     *
     * @constructor Create [ChangeTabPosition]
     *
     * @param position New position for view pager
     */
    internal data class ChangeTabPosition(val position: Int) : WelcomeIntent()

    /**
     * Show role screen.
     */
    internal object ShowRoleScreen : WelcomeIntent()

    /**
     * Show school screen.
     */
    internal object ShowSchoolScreen : WelcomeIntent()

    /**
     * Show professor screen.
     */
    internal object ShowProfessorScreen : WelcomeIntent()
}