package com.android.common.models.schedule

/**
 * Day.
 *
 * @property date Current date in text format
 * @property lessons Lessons
 * @constructor Create [Day]
 */
data class Day(
    val date: String,
    val lessons: List<Lesson>
)