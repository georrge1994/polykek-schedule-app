package com.android.schedule.controller.api.week.lesson

import com.android.common.models.map.BuildingResponse
import com.google.gson.annotations.SerializedName

/**
 * Audience response.
 *
 * @property id Audience id
 * @property name Audience name
 * @property building Building name
 * @constructor Create [AudienceResponse]
 */
data class AudienceResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("building") val building: BuildingResponse? = null
)