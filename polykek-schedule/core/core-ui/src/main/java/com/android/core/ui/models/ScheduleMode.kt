package com.android.core.ui.models

import android.os.Bundle

/**
 * Different schedule modes are used to change default behaviours and navigation for start screens.
 *
 * @constructor Create empty constructor for schedule mode
 */
enum class ScheduleMode { WELCOME, NEW_ITEM, SEARCH }

private const val SCHEDULE_MODE = "SCHEDULE_MODE"

/**
 * Get bundle.
 *
 * @param scheduleMode Schedule mode
 * @return Arguments with current schedule mode
 */
fun getScheduleModeBundle(scheduleMode: ScheduleMode) = Bundle().apply {
    putInt(SCHEDULE_MODE, scheduleMode.ordinal)
}

/**
 * Get [ScheduleMode] from [Bundle].
 *
 * @receiver [Bundle]
 * @return [ScheduleMode]
 */
fun Bundle.getScheduleMode(): ScheduleMode = ScheduleMode.values()[getInt(SCHEDULE_MODE)]