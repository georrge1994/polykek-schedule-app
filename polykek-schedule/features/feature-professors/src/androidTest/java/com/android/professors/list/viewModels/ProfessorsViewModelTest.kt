package com.android.professors.list.viewModels

import com.android.common.models.professors.Professor
import com.android.common.models.schedule.Week
import com.android.professors.list.useCases.ProfessorsUseCase
import com.android.schedule.controller.api.IScheduleController
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.getOrAwaitValue
import com.android.test.support.dataGenerator.LessonDataGenerator
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.testFixtures.waitActiveSubscription
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

/**
 * Professors view model test for [ProfessorsViewModel].
 *
 * @constructor Create empty constructor for professors view model test
 */
class ProfessorsViewModelTest : BaseViewModelUnitTest() {
    private val lessonDataGenerator = LessonDataGenerator()
    private val professors = listOf<Professor>(mockk())
    private val weekFlowMock = MutableStateFlow<Week?>(null)
    private val scheduleController: IScheduleController = mockk {
        coEvery { weekFlow } returns weekFlowMock
    }
    private val professorsUseCase: ProfessorsUseCase = mockk {
        coEvery { getProfessors(any()) } returns professors
    }
    private val professorsViewModel = ProfessorsViewModel(scheduleController, professorsUseCase)

    /**
     * Complex test.
     */
    @Test
    fun complexTest() = runBlockingUnit {
        professorsViewModel.asyncSubscribe().joinWithTimeout()
        weekFlowMock.waitActiveSubscription().emitAndWait(lessonDataGenerator.getWeekMockk()).joinWithTimeout()
        professorsViewModel.professors.getOrAwaitValue(professors)
        professorsViewModel.isListEmpty.getOrAwaitValue(false)
        professorsViewModel.unSubscribe()
    }

    /**
     * Complex test is empty.
     */
    @Test
    fun complexTestIsEmpty() = runBlockingUnit {
        professorsViewModel.asyncSubscribe().joinWithTimeout()
        weekFlowMock.waitActiveSubscription().emitAndWait(null).joinWithTimeout()
        professorsViewModel.isListEmpty.getOrAwaitValue(true)
        professorsViewModel.unSubscribe()
    }
}