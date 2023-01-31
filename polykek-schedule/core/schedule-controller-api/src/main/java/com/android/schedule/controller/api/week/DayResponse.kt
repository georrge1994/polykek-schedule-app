package com.android.schedule.controller.api.week

import com.android.schedule.controller.api.week.lesson.LessonResponse
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Day response.
 *
 * @property weekday Index of week day
 * @property date Date in ScheduleDateUseCase.PERIOD_FORMAT
 * @property lessons Lessons (Mutable special for Harry Potter feature)
 * @constructor Create [DayResponse]
 */
data class DayResponse(
    @SerializedName("weekday")  val weekday: Int = 0,
    @SerializedName("date")     val date: String? = null,
    @SerializedName("lessons")  val lessons: MutableList<LessonResponse> = Collections.emptyList()
)