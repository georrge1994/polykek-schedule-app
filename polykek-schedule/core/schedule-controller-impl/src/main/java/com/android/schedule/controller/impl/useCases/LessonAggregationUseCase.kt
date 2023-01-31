package com.android.schedule.controller.impl.useCases

import com.android.schedule.controller.api.week.lesson.LessonResponse
import com.android.shared.code.utils.markers.IUseCase
import javax.inject.Inject

internal const val FIRST = "I - "
internal const val SECOND = "II - "

/**
 * Lesson aggregation use case.
 *
 * @property addressAggregationUseCase Use case for aggregating lesson address
 * @property joinResponsesUseCase Join addresses use case
 * @constructor Create [LessonAggregationUseCase]
 */
internal class LessonAggregationUseCase @Inject constructor(
    private val addressAggregationUseCase: AddressAggregationUseCase,
    private val joinResponsesUseCase: JoinResponsesUseCase
) : IUseCase {
    /**
     * Just clone and prepare.
     *
     * @param lesson Lesson
     * @return [LessonResponse]
     */
    internal fun justCloneAndPrepare(lesson: LessonResponse): LessonResponse = lesson.clone().apply {
        correctAddress = addressAggregationUseCase.getCorrectedAddress(lesson.auditories?.firstOrNull())
        correctTeacherName = joinResponsesUseCase.getCorrectedTeacherName(lesson.teachers?.firstOrNull())
    }

    /**
     * Join lessons.
     *
     * @param lesson1 Lesson 1
     * @param lesson2 Lesson 2
     * @return Lesson, which combined from [lesson1] and [lesson2]
     */
    internal fun joinLessons(lesson1: LessonResponse, lesson2: LessonResponse): LessonResponse = lesson1.copy(
        typeObj = lesson1.typeObj?.copy(name = joinResponsesUseCase.getJoinedTypeName(lesson1, lesson2))
    ).apply {
        correctAddress = joinResponsesUseCase.getJoinedAddress(lesson1, lesson2)
        correctTeacherName = joinResponsesUseCase.getJoinedTeacherName(lesson1, lesson2)
    }
}