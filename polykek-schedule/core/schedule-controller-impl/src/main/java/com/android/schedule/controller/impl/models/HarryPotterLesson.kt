package com.android.schedule.controller.impl.models

/**
 * Harry potter lesson.
 *
 * @property title Harry Potter lesson name
 * @property teacher Harry Potter teacher name
 * @property place Harry Potter place name
 * @constructor Create [HarryPotterLesson]
 */
internal data class HarryPotterLesson(
    val title: String,
    val teacher: String,
    val place: String
)