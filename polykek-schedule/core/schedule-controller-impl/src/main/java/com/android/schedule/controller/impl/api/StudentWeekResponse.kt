package com.android.schedule.controller.impl.api

import androidx.annotation.Keep
import com.android.common.models.groups.GroupResponse
import com.android.schedule.controller.api.week.DayResponse
import com.android.schedule.controller.api.week.IWeekResponse
import com.android.schedule.controller.api.week.WeekInfoResponse
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Student week response.
 *
 * @property weekInfo Information about week
 * @property days Schedule by days
 * @property group Information about group
 * @constructor Create [StudentWeekResponse]
 */
@Keep
internal data class StudentWeekResponse(
    @SerializedName("week")     override val weekInfo: WeekInfoResponse = WeekInfoResponse(),
    @SerializedName("days")     override val days: MutableList<DayResponse> = Collections.emptyList(),
    @SerializedName("group")    val group: GroupResponse = GroupResponse()
) : IWeekResponse