package com.android.schedule.controller.impl

import com.android.common.models.savedItems.SavedItem
import com.android.common.models.schedule.Week
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.schedule.controller.impl.support.dataGenerator.LessonDataGenerator
import com.android.schedule.controller.impl.useCases.ScheduleDateUseCase
import com.android.schedule.controller.impl.useCases.ScheduleUseCase
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.testFixtures.waitActiveSubscription
import com.android.test.support.unitTest.base.BaseUnitTestForSubscriptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.*
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Schedule controller test for [ScheduleController].
 *
 * @constructor Create empty constructor for schedule controller test
 */
class ScheduleControllerTest : BaseUnitTestForSubscriptions() {
    private val lessonDataGenerator = LessonDataGenerator()
    private val saveItemFlow = MutableSharedFlow<SavedItem?>()
    private val noteIdsFlow = MutableSharedFlow<List<String>?>()
    private val weekMockk: Week = lessonDataGenerator.getWeekMockk()

    private val scheduleUseCase: ScheduleUseCase = mockk {
        coEvery { getSchedule(any(), any()) } returns weekMockk
    }
    private val notesRoomRepository: INotesRoomRepository = mockk {
        coEvery { getNoteIdsFlow() } returns noteIdsFlow
    }
    private val savedItemsRoomRepository: ISavedItemsRoomRepository = mockk {
        coEvery { getSelectedItemFlow() } returns saveItemFlow
    }
    private val scheduleDateUseCase: ScheduleDateUseCase = mockk {
        coEvery { getPeriod() } returns "01.01.2018"
    }

    private val scheduleController = ScheduleController(
        scheduleUseCase = scheduleUseCase,
        notesRoomRepository = notesRoomRepository,
        savedItemsRoomRepository = savedItemsRoomRepository,
        scheduleDateUseCase = scheduleDateUseCase
    )

    /**
     * Complex test.
     */
    @Test
    fun complexTest() = runBlockingUnit {
        val savedItem = SavedItem(0, "1083/1")
        saveItemFlow.waitActiveSubscription().emitAndWait(savedItem).joinWithTimeout()
        noteIdsFlow.waitActiveSubscription()
        coVerify(exactly = 1) {
            scheduleUseCase.getSchedule(any(), any())
            scheduleDateUseCase.getPeriod()
        }
        scheduleController.weekFlow.subscribeAndCompareFirstValue(weekMockk)
        assertEquals(savedItem, scheduleController.selectedItem)
    }

    /**
     * Complex test 2.
     */
    @Test
    fun complexTest2() = runBlockingUnit {
        val savedItem = SavedItem(0, "1083/1")
        saveItemFlow.waitActiveSubscription().emitAndWait(savedItem).joinWithTimeout()
        coVerify(exactly = 1) {
            scheduleUseCase.getSchedule(any(), any())
            scheduleDateUseCase.getPeriod()
        }
        noteIdsFlow.waitActiveSubscription().emitAndWait(listOf("noteId")).joinWithTimeout()
        // Check that week was updated.
        scheduleController.weekFlow.take(1)
            .onEach {
                assertEquals(true, it?.days?.get(0)?.lessons?.first()?.withNotes)
            }.flowOn(unconfinedTestDispatcher)
            .launchIn(testScope)
            .joinWithTimeout()
    }

    /**
     * Update schedule.
     */
    @Test
    fun updateSchedule() = runBlockingUnit {
        val savedItem = SavedItem(0, "1083/1")
        saveItemFlow.waitActiveSubscription().emitAndWait(savedItem).joinWithTimeout()
        scheduleController.updateSchedule("10.02.2022")
        coVerify(exactly = 2) { scheduleUseCase.getSchedule(any(), any()) }
        coVerify(exactly = 1) { scheduleDateUseCase.getPeriod() }
        scheduleController.weekFlow.subscribeAndCompareFirstValue(weekMockk)
    }
}