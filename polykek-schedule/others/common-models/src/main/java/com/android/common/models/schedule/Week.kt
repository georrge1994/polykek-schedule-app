package com.android.common.models.schedule

/**
 * Week.
 *
 * @property title Time interval of week
 * @property days Days
 * @constructor Create [Week]
 */
data class Week(
    var title: String,
    var days: Map<Int, Day>
)