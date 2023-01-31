package com.android.schedule.controller.impl.useCases

import com.android.common.models.schedule.Day
import com.android.common.models.schedule.Week
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.schedule.controller.impl.support.dataGenerator.LessonDataGenerator
import com.android.test.support.androidTest.BaseAndroidUnitTest
import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Schedule response convert use case test for [ScheduleResponseConvertUseCase].
 *
 * @constructor Create empty constructor for schedule response convert use case test
 */
class ScheduleResponseConvertUseCaseTest : BaseAndroidUnitTest() {
    private val lessonDataGenerator = LessonDataGenerator()
    private val notesRoomRepository: INotesRoomRepository = mockk {
        coEvery { getNoteIds() } returns listOf()
    }
    private val lessonConvertUseCase: LessonConvertFormatUseCase = mockk {
        coEvery { convertToLessonFormat(any(), any(), any()) } returns ArrayList()
    }
    private val scheduleResponseConvertUseCase = ScheduleResponseConvertUseCase(
        application = application,
        notesRoomRepository = notesRoomRepository,
        lessonConvertUseCase = lessonConvertUseCase
    )

    /**
     * Convert to week format.
     */
    @Test
    fun convertToWeekFormat() = runBlockingUnit {
        val professorResponse = lessonDataGenerator.getProfessorWeekResponse()
        val result = scheduleResponseConvertUseCase.convertToWeekFormat(professorResponse)
        coVerify(exactly = 1) {
            notesRoomRepository.getNoteIds()
            lessonConvertUseCase.convertToLessonFormat(any(), any(), any())
        }
        assertEquals(
            Week(title = TEST_STRING, days = mapOf(0 to Day(date = "", lessons = emptyList()))),
            result
        )
    }

    /**
     * Convert to week format 2.
     */
    @Test
    fun convertToWeekFormat2() = runBlockingUnit {
        val professorResponse = lessonDataGenerator.getProfessorWeekResponse()
        val result = scheduleResponseConvertUseCase.convertToWeekFormat(professorResponse, 12)
        coVerify(exactly = 1) {
            notesRoomRepository.getNoteIds()
            lessonConvertUseCase.convertToLessonFormat(any(), any(), any())
        }
        assertEquals(
            Week(title = TEST_STRING, days = mapOf(0 to Day(date = "", lessons = emptyList()))),
            result
        )
    }

    /**
     * Convert teacher to professor.
     */
    @Test
    fun convertTeacherToProfessor() {
        coEvery { lessonConvertUseCase.convertTeacherToProfessor(any()) } returns mockk()
        scheduleResponseConvertUseCase.convertTeacherToProfessor(mockk())
        coVerify(exactly = 1) { lessonConvertUseCase.convertTeacherToProfessor(any()) }
    }
}