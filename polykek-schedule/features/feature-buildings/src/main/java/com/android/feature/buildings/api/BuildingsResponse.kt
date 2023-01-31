package com.android.feature.buildings.api

import com.android.common.models.map.BuildingResponse
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Buildings response.
 *
 * @property buildingResponses Buildings
 * @constructor Create [BuildingsResponse]
 */
internal data class BuildingsResponse(
    @SerializedName("buildings") val buildingResponses: List<BuildingResponse> = Collections.emptyList()
)