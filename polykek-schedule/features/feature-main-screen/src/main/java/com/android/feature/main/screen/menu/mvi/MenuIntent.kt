package com.android.feature.main.screen.menu.mvi

import com.android.core.ui.mvi.MviIntent
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * Menu intent.
 *
 * @constructor Create empty constructor for menu intent
 */
internal sealed class MenuIntent : MviIntent {
    /**
     * Change state of bottom bar.
     *
     * @property newState [BottomSheetBehavior.STATE_EXPANDED] or [BottomSheetBehavior.STATE_COLLAPSED]
     * @constructor Create [ChangeStateOfBottomBar]
     */
    internal data class ChangeStateOfBottomBar(val newState: Int) : MenuIntent()

    /**
     * Update ui by offset.
     *
     * @property offset Offset of bottom bar [0f..1f]
     * @constructor Create [UpdateUiByOffset]
     */
    internal data class UpdateUiByOffset(val offset: Float) : MenuIntent()
}