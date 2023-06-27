package com.android.core.ui.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import com.android.core.ui.mvi.MviAction
import com.android.core.ui.mvi.MviIntent
import com.android.core.ui.mvi.MviState
import com.android.core.ui.mvi.MviViewModel

/**
 * Vertical fragment blocks rotation in the vertical mode.
 *
 * @constructor Create empty constructor for vertical fragment
 */
abstract class VerticalFragment<I : MviIntent, S : MviState, A : MviAction, VM : MviViewModel<I, S, A>> :
    NavigationFragment<I, S, A, VM>() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }
}