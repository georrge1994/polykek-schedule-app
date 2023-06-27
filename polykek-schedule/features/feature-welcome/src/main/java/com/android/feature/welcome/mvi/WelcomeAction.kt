package com.android.feature.welcome.mvi

import com.android.core.ui.mvi.MviAction

/**
 * Welcome action.
 *
 * @constructor Create empty constructor for welcome action
 */
internal sealed class WelcomeAction : MviAction {
    /**
     * Show role screen.
     */
    internal object ShowRoleScreen : WelcomeAction()

    /**
     * Show school screen.
     */
    internal object ShowSchoolScreen : WelcomeAction()

    /**
     * Show professor screen.
     */
    internal object ShowProfessorScreen : WelcomeAction()
}