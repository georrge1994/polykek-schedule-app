package com.android.feature.main.screen.menu.mvi

import com.android.core.ui.mvi.MviAction
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * Menu action.
 *
 * @constructor Create empty constructor for menu action
 */
internal sealed class MenuAction : MviAction {
    /**
     * Change menu state.
     *
     * @property state [BottomSheetBehavior.STATE_EXPANDED] or [BottomSheetBehavior.STATE_COLLAPSED]
     * @constructor Create [ChangeMenuState]
     */
    internal data class ChangeMenuState(val state: Int) : MenuAction()
}
