package com.android.feature.welcome.viewModels

import com.android.core.ui.viewModels.BaseViewModel
import com.android.feature.welcome.R
import javax.inject.Inject

/**
 * Welcome view model contains simple logic for shoot-fragments.
 *
 * @constructor Create empty constructor for welcome view model
 */
internal class WelcomeViewModel @Inject constructor() : BaseViewModel() {
    /**
     * Get drawable id.
     *
     * @param position Position
     * @return Drawable resource
     */
    internal fun getDrawableId(position: Int): Int = when (position) {
        0 -> R.drawable.welcome_0
        1 -> R.drawable.welcome_1
        2 -> R.drawable.welcome_2
        else -> R.drawable.welcome_3
    }

    /**
     * Get title res id.
     *
     * @param position Position
     * @return Label resource
     */
    internal fun getLabelResId(position: Int): Int = when (position) {
        0 -> R.string.welcome_screen_message_0
        1 -> R.string.welcome_screen_message_1
        2 -> R.string.welcome_screen_message_2
        else -> R.string.welcome_screen_message_3
    }
}