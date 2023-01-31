package com.android.schedule.controller.impl.useCases

import android.app.Application
import com.android.schedule.controller.api.week.lesson.LessonResponse
import com.android.schedule.controller.api.week.lesson.TeacherResponse
import com.android.schedule.controller.impl.R
import com.android.shared.code.utils.markers.IUseCase
import javax.inject.Inject

/**
 * Join responses use case.
 *
 * @property application Application object to get context
 * @property addressAggregationUseCase Use case for aggregating lesson address
 * @constructor Create [JoinResponsesUseCase]
 */
internal class JoinResponsesUseCase @Inject constructor(
    private val application: Application,
    private val addressAggregationUseCase: AddressAggregationUseCase
) : IUseCase {
    /**
     * Join teachers.
     *
     * @param lesson1 Lesson 1
     * @param lesson2 Lesson 2
     * @return Correct teacher name
     */
    internal fun getJoinedTeacherName(lesson1: LessonResponse, lesson2: LessonResponse): String {
        val teacher1 = lesson1.teachers?.firstOrNull()
        val teacher2 = lesson2.teachers?.firstOrNull()
        val correctedTeacherName1 = getCorrectedTeacherName(teacher1)
        return if (teacher1 == teacher2) {
            correctedTeacherName1
        } else {
            "$FIRST$correctedTeacherName1; $SECOND${getCorrectedTeacherName(teacher2)}"
        }
    }

    /**
     * Get corrected teacher name.
     *
     * @receiver [TeacherResponse]
     * @return Correct teacher name. If teacher contains all information, will provide complex name, else more simplified variant
     */
    internal fun getCorrectedTeacherName(teacherResponse: TeacherResponse?): String = with(teacherResponse) {
        when {
            !this?.grade.isNullOrBlank() && !this?.fullName.isNullOrBlank() -> "${this?.grade} ${this?.fullName}"
            !this?.fullName.isNullOrBlank() -> "${this?.fullName}"
            else -> application.getString(R.string.schedule_fragment_voldemort)
        }
    }

    /**
     * Join addresses.
     *
     * @param lesson1 Lesson 1
     * @param lesson2 Lesson 2
     * @return Correct address
     */
    internal fun getJoinedAddress(lesson1: LessonResponse, lesson2: LessonResponse): String = with(addressAggregationUseCase) {
        lesson1.auditories?.firstOrNull().let { auditorium1 ->
            lesson2.auditories?.firstOrNull().let { auditorium2 ->
                when {
                    // Different buildings -> will show both addresses.
                    auditorium1?.building?.name != auditorium2?.building?.name ->
                        "$FIRST${getCorrectedAddress(auditorium1)}; $SECOND${getCorrectedAddress(auditorium2)}"
                    // Same building, but different auditoriums -> will show combined address.
                    auditorium1?.name != auditorium2?.name -> getCombinedCorrectAddress(auditorium1, auditorium2)
                    // Identical addresses -> will show the one of them.
                    else -> getCorrectedAddress(auditorium1)
                }
            }
        }
    }

    /**
     * Get joined type name.
     *
     * @param lesson1 Lesson 1
     * @param lesson2 Lesson 2
     * @return Joined lesson type name for both lessons
     */
    internal fun getJoinedTypeName(lesson1: LessonResponse, lesson2: LessonResponse): String? = when {
        lesson1.typeObj != null && lesson2.typeObj != null && lesson1.typeObj!!.name != lesson2.typeObj!!.name ->
            "${lesson1.typeObj!!.name}, ${lesson2.typeObj!!.name}"
        lesson1.typeObj != null -> lesson1.typeObj!!.name
        lesson2.typeObj != null -> lesson2.typeObj!!.name
        else -> null
    }
}