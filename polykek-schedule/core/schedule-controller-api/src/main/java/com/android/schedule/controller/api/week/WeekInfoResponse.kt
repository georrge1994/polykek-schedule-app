package com.android.schedule.controller.api.week

import com.google.gson.annotations.SerializedName

/**
 * Week info response.
 *
 * @property dateStart Start week
 * @property dateEnd End week
 * @property isOdd Is odd = true, even is false
 * @constructor Create [WeekInfoResponse]
 */
data class WeekInfoResponse(
    @SerializedName("date_start")   val dateStart: String? = null,
    @SerializedName("date_end")     val dateEnd: String? = null,
    @SerializedName("is_odd")       val isOdd: Boolean = false
)