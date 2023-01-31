package com.android.test.support.dataGenerator

import com.android.common.models.schedule.Day
import com.android.common.models.schedule.Lesson
import com.android.common.models.schedule.Week

/**
 * Lesson data generator.
 *
 * @constructor Create empty constructor for lesson data generator
 */
class LessonDataGenerator {
    /**
     * Get week mockk.
     *
     * @return Test week
     */
    fun getWeekMockk() = Week(
        title = "test week",
        days = mapOf(
            0 to Day(date = "Monday", lessons = listOf(getLessonMockk(), getLessonMockk()))
        )
    )

    /**
     * Get lesson.
     *
     * @return Test lesson
     */
    fun getLessonMockk() = Lesson(
        time = "time",
        title = "title",
        address = "address",
        buildingNames = "buildingNames",
        typeLesson = "typeLesson",
        teacherNames = "teacherNames",
        withNotes = false,
        noteId = "noteId",
        groupId = 123,
        professors = emptyList()
    )
}