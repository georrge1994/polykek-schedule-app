package com.android.schedule.controller.impl.api

import com.android.schedule.controller.api.week.DayResponse
import com.android.schedule.controller.api.week.IWeekResponse
import com.android.schedule.controller.api.week.WeekInfoResponse
import com.android.schedule.controller.api.week.lesson.TeacherResponse
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Professor week response.
 *
 * @property weekInfo Information about week
 * @property days Schedule by days
 * @property teacher Information about teacher
 * @constructor Create [ProfessorWeekResponse]
 */
internal data class ProfessorWeekResponse(
    @SerializedName("week")         override val weekInfo: WeekInfoResponse = WeekInfoResponse(),
    @SerializedName("days")         override val days: List<DayResponse> = Collections.emptyList(),
    @SerializedName("teacher")      val teacher: TeacherResponse = TeacherResponse()
) : IWeekResponse