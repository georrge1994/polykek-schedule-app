package com.android.schedule.controller.impl.support.dataGenerator

import com.android.common.models.map.BuildingResponse
import com.android.schedule.controller.api.week.DayResponse
import com.android.schedule.controller.api.week.WeekInfoResponse
import com.android.schedule.controller.api.week.lesson.AudienceResponse
import com.android.schedule.controller.api.week.lesson.LessonResponse
import com.android.schedule.controller.api.week.lesson.LessonTypeResponse
import com.android.schedule.controller.api.week.lesson.TeacherResponse
import com.android.schedule.controller.impl.api.ProfessorWeekResponse
import com.android.test.support.testFixtures.TestDataGenerator

/**
 * Lesson and week data generator for Unit tests.
 */
internal class LessonDataGenerator : TestDataGenerator {
    /**
     * Get professor week response.
     *
     * @return Mockk of [ProfessorWeekResponse]
     */
    internal fun getProfessorWeekResponse() = ProfessorWeekResponse(
        weekInfo = WeekInfoResponse(
            dateStart = "2022.11.07",
            dateEnd = "2022.11.13",
            isOdd = true
        ),
        days = mutableListOf(
            DayResponse(
                weekday = 1,
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
     * @param fullName Full name
     * @param buildingName Name of building
     * @param audienceName Name of audience
     * @return [LessonResponse]
     */
    internal fun getLessonResponse(
        fullName: String? = "Миколойчук В.А.",
        buildingName: String = "Главное Здание",
        audienceName: String = "ГЗ",
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
                fullName = fullName,
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
                name = audienceName,
                building = BuildingResponse(
                    id = 1,
                    name = buildingName,
                    abbr = "ГЗ",
                    address = "102"
                )
            )
        )
    )
}