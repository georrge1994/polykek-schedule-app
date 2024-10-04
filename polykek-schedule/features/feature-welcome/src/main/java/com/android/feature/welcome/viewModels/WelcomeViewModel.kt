package com.android.feature.welcome.viewModels

import com.android.core.ui.mvi.MviViewModel
import com.android.feature.welcome.R
import com.android.feature.welcome.mvi.WelcomeAction
import com.android.feature.welcome.mvi.WelcomeIntent
import com.android.feature.welcome.mvi.WelcomeState
import javax.inject.Inject

/**
 * Welcome view model contains simple logic for shoot-fragments.
 *
 * @constructor Create empty constructor for welcome view model
 */
internal class WelcomeViewModel @Inject constructor() : MviViewModel<WelcomeIntent, WelcomeState, WelcomeAction>(
    WelcomeState.Default
) {
    override suspend fun dispatchIntent(intent: WelcomeIntent) {
        when (intent) {
            is WelcomeIntent.ChangeTabPosition -> updateTitle(intent.position)
            WelcomeIntent.ShowRoleScreen -> WelcomeAction.ShowRoleScreen.emitAction()
            WelcomeIntent.ShowProfessorScreen -> WelcomeAction.ShowProfessorScreen.emitAction()
            WelcomeIntent.ShowSchoolScreen -> WelcomeAction.ShowSchoolScreen.emitAction()
        }
    }
    
    /**
     * Update title res id.
     *
     * @param position Position
     */
    private suspend fun updateTitle(position: Int) {
        val titleResId = when (position) {
            0 -> R.string.welcome_screen_message_0
            1 -> R.string.welcome_screen_message_1
            2 -> R.string.welcome_screen_message_2
            3 -> R.string.welcome_screen_message_3
            else -> R.string.welcome_screen_message_4
        }
        if (titleResId != currentState.titleResId) {
            currentState.copyState(titleResId).emitState()
        }
    }

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
        3 -> R.drawable.welcome_3
        else -> R.drawable.welcome_4
    }
}