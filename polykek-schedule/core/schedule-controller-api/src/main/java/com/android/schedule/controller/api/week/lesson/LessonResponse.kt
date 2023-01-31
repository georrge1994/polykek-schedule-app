package com.android.schedule.controller.api.week.lesson

import com.android.common.models.groups.GroupResponse
import com.google.gson.annotations.SerializedName

/**
 * Lesson response.
 *
 * @property subject Name
 * @property subjectShort Short name
 * @property timeStart Time start
 * @property timeEnd Time end
 * @property typeObj [LessonTypeResponse]
 * @property groups Groups for which this lesson will be held
 * @property teachers Teachers, which will be hold this lesson
 * @property auditories Auditories, where this lesson will be held
 * @constructor Create [LessonResponse]
 */
data class LessonResponse(
    @SerializedName("subject")          val subject: String? = null,
    @SerializedName("subject_short")    val subjectShort: String? = null,
    @SerializedName("time_start")       val timeStart: String? = null,
    @SerializedName("time_end")         val timeEnd: String? = null,
    @SerializedName("typeObj")          val typeObj: LessonTypeResponse? = null,
    @SerializedName("groups")           val groups: List<GroupResponse>? = null,
    @SerializedName("teachers")         val teachers: List<TeacherResponse>? = null,
    @SerializedName("auditories")       val auditories: List<AudienceResponse>? = null
) : Cloneable {
    var correctAddress: String? = null
    var correctTeacherName: String? = null

    public override fun clone(): LessonResponse {
        return super.clone() as LessonResponse
    }
}