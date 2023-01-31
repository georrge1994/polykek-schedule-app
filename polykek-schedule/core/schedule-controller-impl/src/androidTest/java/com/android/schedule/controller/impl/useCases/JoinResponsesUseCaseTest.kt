package com.android.schedule.controller.impl.useCases

import com.android.schedule.controller.impl.support.dataGenerator.LessonDataGenerator
import com.android.test.support.androidTest.BaseAndroidUnitTest
import com.android.test.support.testFixtures.TEST_STRING
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

private const val CORRECTED_ADDRESS = "CORRECTED_ADDRESS"
private const val CORRECTED_ADDRESS2 = "CORRECTED_ADDRESS2"

/**
 * Join responses use case test for [JoinResponsesUseCase].
 *
 * @constructor Create empty constructor for join responses use case test
 */
class JoinResponsesUseCaseTest : BaseAndroidUnitTest() {
    private val lessonDataGenerator = LessonDataGenerator()
    private val addressAggregationUseCase: AddressAggregationUseCase = mockk {
        coEvery { getCorrectedAddress(any()) } returns CORRECTED_ADDRESS
        coEvery { getCombinedCorrectAddress(any(), any()) } returns CORRECTED_ADDRESS2
    }
    private val joinResponsesUseCase = JoinResponsesUseCase(application, addressAggregationUseCase)

    /**
     * Get joined teacher name 1.
     */
    @Test
    fun getJoinedTeacherName1() {
        val lesson1 = lessonDataGenerator.getLessonResponse()
        val lesson2 = lessonDataGenerator.getLessonResponse()
        assertEquals("grade Миколойчук В.А.", joinResponsesUseCase.getJoinedTeacherName(lesson1, lesson2))
    }

    /**
     * Get joined teacher name 2.
     */
    @Test
    fun getJoinedTeacherName2() {
        val lesson1 = lessonDataGenerator.getLessonResponse()
        val lesson2 = lessonDataGenerator.getLessonResponse(fullName = "Иванько И.В.")
        assertEquals(
            "I - grade Миколойчук В.А.; II - grade Иванько И.В.",
            joinResponsesUseCase.getJoinedTeacherName(lesson1, lesson2)
        )
    }

    /**
     * Get corrected teacher name standard.
     */
    @Test
    fun getCorrectedTeacherName_standard() {
        val lesson1 = lessonDataGenerator.getLessonResponse().teachers?.firstOrNull()
        assertEquals(
            "grade Миколойчук В.А.",
            joinResponsesUseCase.getCorrectedTeacherName(lesson1)
        )
    }

    /**
     * Get corrected teacher name 2 without grade.
     */
    @Test
    fun getCorrectedTeacherName2_withoutGrade() {
        val lesson1 = lessonDataGenerator.getLessonResponse().teachers?.firstOrNull()?.copy(grade = null)
        assertEquals(
            "Миколойчук В.А.",
            joinResponsesUseCase.getCorrectedTeacherName(lesson1)
        )
    }

    /**
     * Get corrected teacher name 2 without name.
     */
    @Test
    fun getCorrectedTeacherName2_withoutName() {
        val lesson1 = lessonDataGenerator.getLessonResponse().teachers?.firstOrNull()?.copy(fullName = null, grade = null)
        assertEquals(TEST_STRING, joinResponsesUseCase.getCorrectedTeacherName(lesson1))
    }

    /**
     * Get joined address same addresses.
     */
    @Test
    fun getJoinedAddress_sameAddresses() {
        val lesson1 = lessonDataGenerator.getLessonResponse()
        assertEquals(CORRECTED_ADDRESS, joinResponsesUseCase.getJoinedAddress(lesson1, lesson1))
    }

    /**
     * Get joined address different building.
     */
    @Test
    fun getJoinedAddress_differentBuilding() {
        val lesson1 = lessonDataGenerator.getLessonResponse()
        val lesson2 = lessonDataGenerator.getLessonResponse(buildingName = "Гидрокорпус")
        assertEquals(
            "I - CORRECTED_ADDRESS; II - CORRECTED_ADDRESS",
            joinResponsesUseCase.getJoinedAddress(lesson1, lesson2)
        )
    }

    /**
     * Get joined address different audiences.
     */
    @Test
    fun getJoinedAddress_differentAudiences() {
        val lesson1 = lessonDataGenerator.getLessonResponse()
        val lesson2 = lessonDataGenerator.getLessonResponse(audienceName = "ГК.")
        assertEquals(
            "CORRECTED_ADDRESS2",
            joinResponsesUseCase.getJoinedAddress(lesson1, lesson2)
        )
    }

    /**
     * Get joined type name different.
     */
    @Test
    fun getJoinedTypeName_different() {
        val lesson1 = lessonDataGenerator.getLessonResponse()
        assertEquals("name", joinResponsesUseCase.getJoinedTypeName(lesson1, lesson1))
    }

    /**
     * Get joined type name nulls.
     */
    @Test
    fun getJoinedTypeName_nulls() {
        val lesson = lessonDataGenerator.getLessonResponse()
        val lessonWithoutAudience = lessonDataGenerator.getLessonResponse().copy(typeObj = null)
        assertEquals("name", joinResponsesUseCase.getJoinedTypeName(lesson, lessonWithoutAudience))
        assertEquals("name", joinResponsesUseCase.getJoinedTypeName(lessonWithoutAudience, lesson))
        assertEquals(null, joinResponsesUseCase.getJoinedTypeName(lessonWithoutAudience, lessonWithoutAudience))
    }
}