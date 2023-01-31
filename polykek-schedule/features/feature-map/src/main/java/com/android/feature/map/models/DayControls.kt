package com.android.feature.map.models

/**
 * Day controls.
 *
 * @property isPreviousEnabled Is previous button enabled
 * @property isNextEnabled Is next button enabled
 * @property dayIndex Index of day week (0..6)
 * @constructor Create [DayControls]
 */
internal data class DayControls(
    val isPreviousEnabled: Boolean,
    val isNextEnabled: Boolean,
    val dayIndex: Int
)