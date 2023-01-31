package com.android.common.models.groups

import com.google.gson.annotations.SerializedName

/**
 * Group response.
 *
 * @property id Group id
 * @property name Number of group
 * @property type Type (common, distance)
 * @property specialization Specialization
 * @property level There are the [level]-th year university students
 * @property kind Unknown information (0, 1)
 * @constructor Create [GroupResponse]
 */
data class GroupResponse(
    @SerializedName("id")       val id: Int = 0,
    @SerializedName("name")     val name: String = "",
    @SerializedName("type")     val type: String? = null,
    @SerializedName("spec")     val specialization: String? = null,
    @SerializedName("level")    val level: Int = 0,
    @SerializedName("kind")     val kind: String = ""
)
