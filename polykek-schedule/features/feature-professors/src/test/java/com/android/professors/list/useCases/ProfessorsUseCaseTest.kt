package com.android.professors.list.useCases

import com.android.test.support.dataGenerator.LessonDataGenerator
import com.android.test.support.unitTest.BaseUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Professors use case test for [ProfessorsUseCase].
 *
 * @constructor Create empty constructor for professors use case test
 */
class ProfessorsUseCaseTest : BaseUnitTest() {
    private val lessonDataGenerator = LessonDataGenerator()
    private val weekMockk = lessonDataGenerator.getWeekMockk()
    private val professorsUseCase = ProfessorsUseCase()

    /**
     * Get professors.
     */
    @Test
    fun getProfessors() {
        assertEquals(
            lessonDataGenerator.getLessonMockk().professors,
            professorsUseCase.getProfessors(weekMockk)
        )
    }
}