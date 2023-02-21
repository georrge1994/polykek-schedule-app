package com.android.common.models.schedule

val stubWeek = Week(title = "...", days = emptyMap())

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