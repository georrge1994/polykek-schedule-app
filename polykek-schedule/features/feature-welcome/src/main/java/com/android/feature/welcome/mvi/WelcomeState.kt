package com.android.feature.welcome.mvi

import androidx.annotation.StringRes
import com.android.core.ui.mvi.MviState
import com.android.feature.welcome.R

/**
 * Welcome state properties.
 */
internal interface WelcomeStateProperties {
    val titleResId: Int
}

/**
 * Welcome state.
 *
 * @constructor Create empty constructor for welcome state
 */
internal sealed class WelcomeState : MviState, WelcomeStateProperties {
    /**
     * Default state with first title.
     */
    internal data object Default : WelcomeState() {
        @StringRes
        override val titleResId: Int = R.string.welcome_screen_message_0
    }

    /**
     * New title.
     *
     * @constructor Create [Update]
     *
     * @param titleResId
     */
    internal class Update(@StringRes override val titleResId: Int) : WelcomeState()

    /**
     * Copy state.
     *
     * @param titleResId Title res id
     * @return [Update]
     */
    internal fun copyState(titleResId: Int = this.titleResId) = Update(titleResId)
}