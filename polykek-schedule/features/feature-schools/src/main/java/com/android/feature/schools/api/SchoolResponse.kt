package com.android.feature.schools.api

import com.google.gson.annotations.SerializedName

/**
 * School response.
 *
 * @property id School id
 * @property name School name
 * @property abbr Abbreviation of school
 * @constructor Create [SchoolResponse]
 */
internal data class SchoolResponse(
    @SerializedName("id") var id: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("abbr") var abbr: String = ""
)