package com.android.schedule.controller.impl.support.dataGenerator

import com.android.common.models.groups.GroupResponse
import com.android.common.models.map.BuildingResponse
import com.android.schedule.controller.api.week.DayResponse
import com.android.schedule.controller.api.week.WeekInfoResponse
import com.android.schedule.controller.api.week.lesson.AudienceResponse
import com.android.schedule.controller.api.week.lesson.LessonResponse
import com.android.schedule.controller.api.week.lesson.LessonTypeResponse
import com.android.schedule.controller.api.week.lesson.TeacherResponse
import com.android.schedule.controller.impl.api.StudentWeekResponse

/**
 * Harry potter lesson mockk data generator.
 *
 * @constructor Create empty constructor for harry potter lesson mockk data generator
 */
internal class HarryPotterLessonMockkDataGenerator {
    /**
     * Get harry potter variant.
     *
     * @return Harry style [StudentWeekResponse]
     */
    internal fun getHarryPotterVariant() = StudentWeekResponse(
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
                    LessonResponse(
                        subject = "subject",
                        subjectShort = "Заклинания",
                        timeStart = "timeStart",
                        timeEnd = "timeEnd",
                        typeObj = LessonTypeResponse(id = 0, name = "name", abbr = "abbr"),
                        groups = emptyList(),
                        teachers = listOf(
                            TeacherResponse(
                                id = 1,
                                fullName = "Филиус Флитвик",
                                firstName = "Валерий",
                                middleName = "Миколойчук",
                                lastName = "Анатольевич",
                                grade = "grade",
                                chair = "Школа Чародейства и Волшебства Хогвартс"
                            )
                        ),
                        auditories = listOf(
                            AudienceResponse(
                                id = 1,
                                name = "Класс заклинаний",
                                building = BuildingResponse(id = 1, name = "Главное Здание", abbr = "ГЗ", address = "102")
                            )
                        )
                    ),
                    LessonResponse(
                        subject = "subject",
                        subjectShort = "История магии",
                        timeStart = "timeStart",
                        timeEnd = "timeEnd",
                        typeObj = LessonTypeResponse(id = 0, name = "name", abbr = "abbr"),
                        groups = emptyList(),
                        teachers = listOf(
                            TeacherResponse(
                                id = 1,
                                fullName = "Почти Безголовый Ник",
                                firstName = "Валерий",
                                middleName = "Миколойчук",
                                lastName = "Анатольевич",
                                grade = "grade",
                                chair = "Школа Чародейства и Волшебства Хогвартс"
                            )
                        ),
                        auditories = listOf(
                            AudienceResponse(
                                id = 1,
                                name = "Класс Истории магии",
                                building = BuildingResponse(id = 1, name = "Главное Здание", abbr = "ГЗ", address = "102")
                            )
                        )
                    ),
                    LessonResponse(
                        subject = "subject",
                        subjectShort = "Трансфигурация",
                        timeStart = "timeStart",
                        timeEnd = "timeEnd",
                        typeObj = LessonTypeResponse(id = 0, name = "name", abbr = "abbr"),
                        groups = emptyList(),
                        teachers = listOf(
                            TeacherResponse(
                                id = 1,
                                fullName = "Минерва Макгонагалл",
                                firstName = "Валерий",
                                middleName = "Миколойчук",
                                lastName = "Анатольевич",
                                grade = "grade",
                                chair = "Школа Чародейства и Волшебства Хогвартс"
                            )
                        ),
                        auditories = listOf(
                            AudienceResponse(
                                id = 1,
                                name = "Класс Трансфигурации",
                                building = BuildingResponse(id = 1, name = "Главное Здание", abbr = "ГЗ", address = "102")
                            )
                        )
                    )
                )
            )
        ),
        group = GroupResponse(
            id = 0,
            name = "",
            type = null,
            specialization = null,
            level = 0,
            kind = ""
        ),
    )
}