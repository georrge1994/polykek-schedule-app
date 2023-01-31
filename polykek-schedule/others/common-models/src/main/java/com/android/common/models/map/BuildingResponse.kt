package com.android.common.models.map

import com.google.gson.annotations.SerializedName

/**
 * Building response.
 *
 * @property id Building id
 * @property name Name
 * @property abbr Short name
 * @property address Address
 * @constructor Create [BuildingResponse]
 */
data class BuildingResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("abbr") val abbr: String? = null,
    @SerializedName("address") val address: String? = null
)