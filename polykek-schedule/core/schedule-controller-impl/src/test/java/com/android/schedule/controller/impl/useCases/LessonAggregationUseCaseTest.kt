package com.android.schedule.controller.impl.useCases

import com.android.schedule.controller.impl.support.dataGenerator.LessonDataGenerator
import com.android.test.support.unitTest.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

private const val CORRECTED_ADDRESS = "correctedAddress"
private const val CORRECTED_TEACHER_NAME = "correctedTeacherName"
private const val JOINED_TEACHER_NAME = "joinedTeacherName"
private const val JOINED_TYPE_NAME = "joinedTypeName"
private const val JOINED_ADDRESS = "joinedAddress"

/**
 * Lesson aggregation use case test for [LessonAggregationUseCase].
 *
 * @constructor Create empty constructor for lesson aggregation use case test
 */
class LessonAggregationUseCaseTest : BaseUnitTest() {
    private val lessonDataGenerator = LessonDataGenerator()
    private val joinResponsesUseCase: JoinResponsesUseCase = mockk {
        coEvery { getCorrectedTeacherName(any()) } returns CORRECTED_TEACHER_NAME
        coEvery { getJoinedTypeName(any(), any()) } returns JOINED_TYPE_NAME
        coEvery { getJoinedAddress(any(), any()) } returns JOINED_ADDRESS
        coEvery { getJoinedTeacherName(any(), any()) } returns JOINED_TEACHER_NAME
    }
    private val addressAggregationUseCase: AddressAggregationUseCase = mockk {
        coEvery { getCorrectedAddress(any()) } returns CORRECTED_ADDRESS
    }
    private val lessonAggregationUseCase = LessonAggregationUseCase(addressAggregationUseCase, joinResponsesUseCase)

    /**
     * Just clone and prepare.
     */
    @Test
    fun justCloneAndPrepare() {
        val result = lessonAggregationUseCase.justCloneAndPrepare(lessonDataGenerator.getLessonResponse())
        assertEquals(CORRECTED_ADDRESS, result.correctAddress)
        assertEquals(CORRECTED_TEACHER_NAME, result.correctTeacherName)
        coVerify(exactly = 1) {
            addressAggregationUseCase.getCorrectedAddress(any())
            joinResponsesUseCase.getCorrectedTeacherName(any())
        }
    }

    /**
     * Join lessons.
     */
    @Test
    fun joinLessons() {
        val lesson1 = lessonDataGenerator.getLessonResponse()
        val lesson2 = lessonDataGenerator.getLessonResponse()
        val result = lessonAggregationUseCase.joinLessons(lesson1, lesson2)
        assertEquals(JOINED_ADDRESS, result.correctAddress)
        assertEquals(JOINED_TEACHER_NAME, result.correctTeacherName)
        assertEquals(JOINED_TYPE_NAME, result.typeObj?.name)
        coVerify(exactly = 1) {
            joinResponsesUseCase.getJoinedAddress(any(), any())
            joinResponsesUseCase.getJoinedTeacherName(any(), any())
            joinResponsesUseCase.getJoinedTypeName(lesson1, lesson2)
        }
    }
}