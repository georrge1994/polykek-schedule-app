package com.android.professors.search.api

import com.android.schedule.controller.api.week.lesson.TeacherResponse
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Teachers response.
 *
 * @property teachers Teachers
 * @constructor Create [TeachersResponse]
 */
internal data class TeachersResponse(
    @SerializedName("teachers") val teachers: List<TeacherResponse> = Collections.emptyList()
)