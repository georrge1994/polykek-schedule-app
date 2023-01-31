package com.android.feature.main.screen.menu.wrappers

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * Hides an useless method.
 *
 * @constructor Create empty constructor for own bottom sheet callback
 */
internal abstract class OwnBottomSheetCallback : BottomSheetBehavior.BottomSheetCallback() {
    override fun onStateChanged(bottomSheet: View, newState: Int) {}
}