package com.android.core.ui.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo

/**
 * Vertical fragment blocks rotation in the vertical mode.
 *
 * @constructor Create empty constructor for vertical fragment
 */
abstract class VerticalFragment : NavigationFragment() {
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