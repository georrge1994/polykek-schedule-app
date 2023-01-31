package com.android.feature.schedule.professor.useCases

import com.android.schedule.controller.api.IProfessorScheduleUseCase
import com.android.test.support.dataGenerator.LessonDataGenerator
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.unitTest.base.BaseUnitTestForSubscriptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Professor schedule use case test for [ProfessorScheduleUseCase].
 *
 * @constructor Create empty constructor for professor schedule use case test
 */
class ProfessorScheduleUseCaseTest : BaseUnitTestForSubscriptions() {
    private val professorScheduleUseCaseFromModule: IProfessorScheduleUseCase = mockk {
        coEvery { getProfessorSchedule(any(), any()) } returns mockk()
    }
    private val professorScheduleUseCase = ProfessorScheduleUseCase(
        professorScheduleUseCase = professorScheduleUseCaseFromModule
    )

    /**
     * Get professor schedule.
     */
    @Test
    fun getProfessorSchedule() = runBlockingUnit {
        val professorId = 0
        val period = "10.10.10"
        professorScheduleUseCase.getProfessorSchedule(professorId, period)
        coVerify(exactly = 1) {
            professorScheduleUseCaseFromModule.getProfessorSchedule(professorId, period)
        }
    }

    /**
     * Get recycler items.
     */
    @Test
    fun getRecyclerItems() {
        val week = LessonDataGenerator().getWeekMockk()
        val result = professorScheduleUseCase.getRecyclerItems(week)
        assertEquals(3, result.size)
        week.days[0]?.apply {
            assertEquals(date, result.first())
            assertEquals(lessons[0], result[1])
            assertEquals(lessons[1], result[2])
        }
    }
}