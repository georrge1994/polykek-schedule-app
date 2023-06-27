package com.android.feature.main.screen.saved.mvi

import com.android.core.ui.mvi.MviState

/**
 * Saved item state properties.
 */
internal interface SavedItemStateProperties : MviState {
    val menuItems: List<Any>
}

/**
 * Saved item state.
 *
 * @constructor Create empty constructor for saved item state
 */
internal sealed class SavedItemState : MviState, SavedItemStateProperties {
    /**
     * Default state.
     */
    internal object Default : SavedItemState() {
        override val menuItems: List<Any> = emptyList()
    }

    /**
     * Update.
     *
     * @property menuItems Menu items
     * @constructor Create [Update]
     */
    internal data class Update(override val menuItems: List<Any>) : SavedItemState()

    /**
     * Copy state.
     *
     * @param menuItems Menu items
     * @return [SavedItemState]
     */
    internal fun copyState(
        menuItems: List<Any> = this.menuItems
    ): SavedItemState = Update(
        menuItems = menuItems
    )
}
