package com.android.feature.schools.api

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Response model for schools.
 *
 * @property faculties List of schools
 * @constructor Create [SchoolsResponse]
 */
internal data class SchoolsResponse(
    @SerializedName("faculties") val faculties: List<SchoolResponse> = Collections.emptyList()
)