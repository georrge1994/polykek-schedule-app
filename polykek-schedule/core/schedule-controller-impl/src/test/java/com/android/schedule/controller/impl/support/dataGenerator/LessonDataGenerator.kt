package com.android.schedule.controller.impl.support.dataGenerator

import com.android.common.models.map.BuildingResponse
import com.android.common.models.professors.Professor
import com.android.common.models.schedule.Day
import com.android.common.models.schedule.Lesson
import com.android.common.models.schedule.Week
import com.android.schedule.controller.api.week.DayResponse
import com.android.schedule.controller.api.week.WeekInfoResponse
import com.android.schedule.controller.api.week.lesson.AudienceResponse
import com.android.schedule.controller.api.week.lesson.LessonResponse
import com.android.schedule.controller.api.week.lesson.LessonTypeResponse
import com.android.schedule.controller.api.week.lesson.TeacherResponse
import com.android.schedule.controller.impl.api.StudentWeekResponse
import com.android.test.support.testFixtures.TestDataGenerator

/**
 * Lesson and week data generator for Unit tests.
 */
internal class LessonDataGenerator : TestDataGenerator {
    /**
     * Get week mockk.
     *
     * @return Test week data
     */
    internal fun getWeekMockk() = Week(
        title = "test week",
        days = mapOf(
            0 to Day(date = "Monday", lessons = listOf(getLessonMockk(), getLessonMockk()))
        )
    )

    /**
     * Get lesson.
     *
     * @return [Lesson]
     */
    internal fun getLessonMockk() = Lesson(
        time = "time",
        title = "title",
        address = "address",
        buildingNames = "buildingNames",
        typeLesson = "typeLesson",
        teacherNames = "teacherNames",
        withNotes = false,
        noteId = "noteId",
        groupId = 123,
        professors = listOf(
            Professor(id = 1, fullName = "Миколойчук В.А.", shortName = "Валерий", chair = ""),
            Professor(id = 2, fullName = "Иванко И.А.", shortName = "Ирина", chair = "")
        )
    )

    /**
     * Get complex week response.
     *
     * @return [StudentWeekResponse]
     */
    internal fun getComplexWeekResponse() = StudentWeekResponse(
        weekInfo = WeekInfoResponse(
            dateStart = "2022.11.07",
            dateEnd = "2022.11.13",
            isOdd = true
        ),
        days = mutableListOf(
            DayResponse(
                weekday = 0,
                date = "2022.11.07",
                lessons = mutableListOf(
                    getLessonResponse(),
                    getLessonResponse(),
                    getLessonResponse()
                )
            )
        )
    )

    /**
     * Get lesson response.
     *
     * @param teacherFullName Teacher full name
     * @param address Address
     * @return Mockk of [LessonResponse]
     */
    internal fun getLessonResponse(
        teacherFullName: String = "Миколойчук В.А.",
        address: String = "102"
    ) = LessonResponse(
        subject = "subject",
        subjectShort = "subjectShort",
        timeStart = "timeStart",
        timeEnd = "timeEnd",
        typeObj = LessonTypeResponse(
            id = 0,
            name = "name",
            abbr = "abbr"
        ),
        groups = emptyList(),
        teachers = listOf(
            TeacherResponse(
                id = 1,
                fullName = teacherFullName,
                firstName = "Валерий",
                middleName = "Миколойчук",
                lastName = "Анатольевич",
                grade = "grade",
                chair = "chair"
            )
        ),
        auditories = listOf(
            AudienceResponse(
                id = 1,
                name = "ГЗ",
                building = BuildingResponse(
                    id = 1,
                    name = "Главное Здание",
                    abbr = "ГЗ",
                    address = address
                )
            )
        )
    )
}