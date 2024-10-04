package com.android.schedule.controller.impl.useCases

import com.android.common.models.api.Resource
import com.android.common.models.professors.Professor
import com.android.common.models.savedItems.SavedItem
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.schedule.controller.impl.api.ScheduleApiRepository
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.unitTest.base.BaseUnitTestForSubscriptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Test

private const val ERROR_MESSAGE = "oops"

/**
 * Schedule use case test for [ScheduleUseCase].
 *
 * @constructor Create empty constructor for schedule use case test
 */
class ScheduleUseCaseTest : BaseUnitTestForSubscriptions() {
    private val scheduleApiRepository: ScheduleApiRepository = mockk {
        coEvery { getProfessorSchedule(any(), any()) } returns Resource.Error(errorMessage = ERROR_MESSAGE)
        coEvery { getScheduleForGroup(any(), any()) } returns Resource.Success(data = mockk {
            coEvery { name } returns "name"
        })
    }
    private val savedItemsRoomRepository: ISavedItemsRoomRepository = mockk {
        coEvery { saveItemAndSelectIt(any()) } returns Unit
    }
    private val scheduleResponseUseCase: ScheduleResponseConvertUseCase = mockk {
        coEvery { convertToWeekFormat(any(), any()) } returns mockk()
    }
    private val harryPotterJokerUseCase: HarryPotterJokerUseCase = mockk {
        coEvery { replaceLessonsWithHarryPotterMemes(any(), any()) } returns mockk()
    }
    private val backgroundMessageBus = MutableSharedFlow<String>(replay = 1)
    private val scheduleUseCase = ScheduleUseCase(
        scheduleApiRepository = scheduleApiRepository,
        savedItemsRoomRepository = savedItemsRoomRepository,
        scheduleResponseUseCase = scheduleResponseUseCase,
        harryPotterJokerUseCase = harryPotterJokerUseCase,
        backgroundMessageBus = backgroundMessageBus
    )

    /**
     * Get schedule group.
     */
    @Test
    fun getSchedule_group() = runBlockingUnit {
        val item = SavedItem(id = 1, name = "nameMultiLines", type = "GROUP", isSelected = false)
        val period = "10.10.10"
        scheduleUseCase.getSchedule(item, period)
        coVerify(exactly = 1) {
            scheduleApiRepository.getScheduleForGroup(item.id, period)
            scheduleResponseUseCase.convertToWeekFormat(any(), item.id)
        }
    }

    /**
     * Check that renaming is working.
     */
    @Test
    fun getSchedule_checkGroupRenaming() = runBlockingUnit {
        val item = SavedItem(id = 1, name = "10832/5", type = "GROUP", isSelected = false)
        val period = "10.10.10"
        scheduleUseCase.getSchedule(item, period)

        val item2 = item.copy(name = "10083020/2")
        scheduleUseCase.getSchedule(item, period)

        coVerify(exactly = 1) {
            savedItemsRoomRepository.saveItemAndSelectIt(any())
        }
    }

    /**
     * Get schedule professor.
     */
    @Test
    fun getSchedule_professor() = runBlockingUnit {
        val item = Professor(id = 1, fullName = "fullName", shortName = "shortName", chair = "chair")
        val period = "10.10.10"
        scheduleUseCase.getSchedule(item.toSavedItem(), period)
        coVerify(exactly = 1) { scheduleApiRepository.getProfessorSchedule(item.id, period) }
        backgroundMessageBus.subscribeAndCompareFirstValue(ERROR_MESSAGE).joinWithTimeout()
    }
}