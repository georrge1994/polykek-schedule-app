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
    internal data object ShowRoleScreen : WelcomeAction()

    /**
     * Show school screen.
     */
    internal data object ShowSchoolScreen : WelcomeAction()

    /**
     * Show professor screen.
     */
    internal data object ShowProfessorScreen : WelcomeAction()
}