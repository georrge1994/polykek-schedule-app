package com.android.schedule.controller.api.week.lesson

import com.google.gson.annotations.SerializedName

/**
 * Lesson type response.
 *
 * @property id Id
 * @property name Type name (lecture, practice)
 * @property abbr Abbreviation
 * @constructor Create [LessonTypeResponse]
 */
data class LessonTypeResponse(
    @SerializedName("id")       val id: Int? = null,
    @SerializedName("name")     val name: String? = null,
    @SerializedName("abbr")     val abbr: String? = null
)