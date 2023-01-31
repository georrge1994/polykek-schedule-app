package com.android.schedule.controller.impl.useCases

import com.android.common.models.professors.Professor
import com.android.common.models.schedule.Lesson
import com.android.schedule.controller.api.week.lesson.LessonResponse
import com.android.schedule.controller.api.week.lesson.TeacherResponse
import com.android.shared.code.utils.general.getZeroIfNull
import com.android.shared.code.utils.markers.IUseCase
import javax.inject.Inject

/**
 * This class converts [LessonResponse] list to [Lesson] list.
 *
 * @property lessonAggregationUseCase Lesson aggregation use case
 * @constructor Create [LessonConvertFormatUseCase]
 */
internal class LessonConvertFormatUseCase @Inject constructor(private val lessonAggregationUseCase: LessonAggregationUseCase) : IUseCase {
    /**
     * Convert [TeacherResponse] to [Professor].
     *
     * @param requestTeacher [TeacherResponse]
     * @return [Professor] or null
     */
    internal fun convertTeacherToProfessor(requestTeacher: TeacherResponse): Professor? = with(requestTeacher) {
        val id = id ?: return null
        val fullName = fullName ?: return null
        val chair = chair ?: "-"
        val shorNameBuilder = StringBuilder()
        if (firstName?.isNotEmpty() == true)
            shorNameBuilder.append(firstName)
        if (middleName?.isNotEmpty() == true) {
            shorNameBuilder.append(' ')
            shorNameBuilder.append(middleName!![0])
            shorNameBuilder.append('.')
        }
        if (lastName?.isNotEmpty() == true) {
            shorNameBuilder.append(lastName!![0])
            shorNameBuilder.append('.')
        }
        return Professor(id, fullName, shorNameBuilder.toString(), chair)
    }

    /**
     * Convert to data-lesson-format to ui-lesson-format.
     *
     * @param lessons Lessons
     * @param noteIds Set of all note ids
     * @param groupId Group id
     * @return UI-lessons for some day
     */
    internal fun convertToLessonFormat(
        lessons: List<LessonResponse>?,
        noteIds: Set<String>,
        groupId: Int? = null
    ) = ArrayList<Lesson>().apply {
        lessons?.let { checkedLessons ->
            checkedLessons.getCombinedLessons().forEach {
                this.add(it.convertToLesson(noteIds, groupId))
            }
        }
    }

    /**
     * Some students` groups are divided to sub-groups with different teachers or lesson's place. Some of lesson separated only
     * in schedule, so we want to get stable format: 1-2 places, 1-2 teachers, 1 lesson.
     *
     * @receiver Lessons for some day
     * @return Joined lessons for one day
     */
    private fun List<LessonResponse>.getCombinedLessons(): List<LessonResponse> {
        if (this.isEmpty())
            return emptyList()
        val newList = ArrayList<LessonResponse>()
        var index = 0
        do {
            val lesson1 = this[index]
            val lesson2 =  this.getOrNull(index + 1)
            if (areSame(lesson1, lesson2) && lesson2 != null) {
                newList.add(lessonAggregationUseCase.joinLessons(lesson1, lesson2))
                index++
            } else {
                newList.add(lessonAggregationUseCase.justCloneAndPrepare(lesson1))
            }
        } while (index++ < this.size - 1)
        return newList
    }

    /**
     * Convert data-model to ui-model.
     *
     * @receiver [LessonResponse]
     * @param noteIds Set of all note ids
     * @param groupId Group id
     * @return [Lesson]
     */
    private fun LessonResponse.convertToLesson(noteIds: Set<String>, groupId: Int?): Lesson {
        val title = subjectShort?.trim() ?: "-"
        val typeLesson = typeObj?.name?.trim() ?: "-"
        val noteId = "${groupId}_${title}_${typeLesson}".replace(" ", "_")
        val professors = teachers?.map { convertTeacherToProfessor(it) } ?: emptyList()
        return Lesson(
            time = "${timeStart}-${timeEnd}".trim(),
            title = title,
            address = correctAddress?.trim() ?: "-",
            buildingNames = auditories?.firstOrNull()?.building?.name,
            typeLesson = typeLesson,
            teacherNames = correctTeacherName?.trim() ?: "-",
            withNotes = noteIds.contains(noteId),
            noteId = noteId,
            groupId = groupId.getZeroIfNull(),
            professors = professors
        )
    }

    /**
     * Check that lessons can be joined.
     *
     * @param lesson1 Lesson 1
     * @param lesson2 Lesson 2
     * @return Condition result
     */
    private fun areSame(lesson1: LessonResponse?, lesson2: LessonResponse?): Boolean =
        lesson1?.timeStart == lesson2?.timeStart && lesson1?.subjectShort == lesson2?.subjectShort
}