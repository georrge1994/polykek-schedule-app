package com.android.schedule.controller.api.week.lesson

import com.google.gson.annotations.SerializedName

/**
 * Teacher response.
 *
 * @property id Teacher id
 * @property fullName Full name
 * @property firstName First name
 * @property middleName Middle name
 * @property lastName Last name
 * @property grade Teacher's grade
 * @property chair Teacher's faculty
 * @constructor Create [TeacherResponse]
 */
data class TeacherResponse(
    @SerializedName("id")           val id: Int? = null,
    @SerializedName("full_name")    val fullName: String? = null,
    @SerializedName("first_name")   val firstName: String? = null,
    @SerializedName("middle_name")  val middleName: String? = null,
    @SerializedName("last_name")    val lastName: String? = null,
    @SerializedName("grade")        val grade: String? = null,
    @SerializedName("chair")        val chair: String? = null
)