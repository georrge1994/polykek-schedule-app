package com.android.schedule.controller.api

import com.android.common.models.professors.Professor
import com.android.schedule.controller.api.week.lesson.TeacherResponse

/**
 * Convert [TeacherResponse] to [Professor].
 */
interface ITeacherToProfessorConvertor {
    /**
     * Convert teacher to professor.
     *
     * @param teacherResponse [TeacherResponse]
     * @return [Professor] or null
     */
    fun convertTeacherToProfessor(teacherResponse: TeacherResponse): Professor?
}