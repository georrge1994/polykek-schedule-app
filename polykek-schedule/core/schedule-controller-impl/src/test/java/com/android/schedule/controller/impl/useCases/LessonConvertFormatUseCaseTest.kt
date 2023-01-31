package com.android.schedule.controller.impl.useCases

import com.android.common.models.professors.Professor
import com.android.common.models.schedule.Lesson
import com.android.schedule.controller.impl.support.dataGenerator.LessonDataGenerator
import com.android.test.support.unitTest.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Lesson convert format use case test for [LessonConvertFormatUseCase].
 *
 * @constructor Create empty constructor for lesson convert format use case test
 */
class LessonConvertFormatUseCaseTest : BaseUnitTest() {
    private val lessonDataGenerator = LessonDataGenerator()
    private val lessonAggregationUseCase: LessonAggregationUseCase = mockk {
        coEvery { justCloneAndPrepare(any()) } returns lessonDataGenerator.getLessonResponse()
        coEvery { joinLessons(any(), any()) } returns lessonDataGenerator.getLessonResponse()
    }
    private val lessonConvertFormatUseCase = LessonConvertFormatUseCase(lessonAggregationUseCase)
    private val expectedResult = Lesson(
        time = "timeStart-timeEnd",
        title = "subjectShort",
        address = "-",
        buildingNames = "Главное Здание",
        typeLesson = "name",
        teacherNames = "-",
        withNotes = false,
        noteId = "1_subjectShort_name",
        groupId = 1,
        professors = listOf(
            Professor(id = 1, fullName = "Миколойчук В.А.", shortName = "Валерий М.А.", chair = "chair")
        )
    )

    /**
     * Convert teacher to professor.
     */
    @Test
    fun convertTeacherToProfessor() {
        val professor = Professor(id = 1, fullName = "Миколойчук В.А.", shortName = "Валерий М.А.", chair = "chair")
        val teacher = lessonDataGenerator.getLessonResponse().teachers?.firstOrNull()!!
        assertEquals(professor, lessonConvertFormatUseCase.convertTeacherToProfessor(teacher))
    }

    /**
     * Convert to lesson format.
     */
    @Test
    fun convertToLessonFormat_justCloneAndPrepare() {
        val result = lessonConvertFormatUseCase.convertToLessonFormat(
            lessons = listOf(lessonDataGenerator.getLessonResponse()),
            noteIds = emptySet(),
            groupId = 1
        )
        assertEquals(listOf(expectedResult), result)
        coVerify(exactly = 1) { lessonAggregationUseCase.justCloneAndPrepare(any()) }
    }

    /**
     * Convert to lesson format 2.
     */
    @Test
    fun convertToLessonFormat2_joinLessons() {
        val result = lessonConvertFormatUseCase.convertToLessonFormat(
            lessons = listOf(
                lessonDataGenerator.getLessonResponse(),
                lessonDataGenerator.getLessonResponse(teacherFullName = "Чеботарев Г.М.", address = "103")
            ),
            noteIds = emptySet(),
            groupId = 1
        )
        assertEquals(listOf(expectedResult), result)
        coVerify(exactly = 1) { lessonAggregationUseCase.joinLessons(any(), any()) }
    }
}