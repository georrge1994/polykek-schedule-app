package com.android.schedule.controller.impl.useCases

import com.android.common.models.api.Resource
import com.android.schedule.controller.impl.api.ScheduleApiRepository
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.unitTest.base.BaseUnitTestForSubscriptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Test

/**
 * Professor schedule use case test for [ProfessorScheduleUseCase].
 *
 * @constructor Create empty constructor for professor schedule use case test
 */
class ProfessorScheduleUseCaseTest : BaseUnitTestForSubscriptions() {
    private val scheduleResponseUseCase: ScheduleResponseConvertUseCase = mockk {
        coEvery { convertToWeekFormat(any()) } returns mockk()
    }
    private val scheduleApiRepository: ScheduleApiRepository = mockk {
        coEvery { getProfessorSchedule(any(), any()) } returns Resource.Success(data = mockk())
    }
    private val backgroundMessageBus = MutableSharedFlow<String>()
    private val professorScheduleUseCase = ProfessorScheduleUseCase(
        scheduleResponseUseCase = scheduleResponseUseCase,
        scheduleApiRepository = scheduleApiRepository,
        backgroundMessageBus = backgroundMessageBus
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
            scheduleApiRepository.getProfessorSchedule(professorId, period)
            scheduleResponseUseCase.convertToWeekFormat(any())
        }
    }
}