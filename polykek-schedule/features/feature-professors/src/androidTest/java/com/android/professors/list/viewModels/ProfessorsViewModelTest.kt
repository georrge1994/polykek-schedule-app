package com.android.professors.list.viewModels

import com.android.common.models.professors.Professor
import com.android.common.models.schedule.Week
import com.android.professors.list.mvi.ProfessorAction
import com.android.professors.list.mvi.ProfessorIntent
import com.android.professors.list.useCases.ProfessorsUseCase
import com.android.schedule.controller.api.IScheduleController
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.dataGenerator.LessonDataGenerator
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.testFixtures.waitActiveSubscription
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Professors view model test for [ProfessorsViewModel].
 *
 * @constructor Create empty constructor for professors view model test
 */
class ProfessorsViewModelTest : BaseViewModelUnitTest() {
    private val lessonDataGenerator = LessonDataGenerator()
    private val professorsMock = listOf(
        Professor(id = 1, fullName = "Миколойчук В.А.", shortName = "Валерий", chair = "")
    )
    private val weekFlowMock = MutableStateFlow<Week?>(null)
    private val scheduleController: IScheduleController = mockk {
        coEvery { weekFlow } returns weekFlowMock
    }
    private val professorsUseCase: ProfessorsUseCase = mockk {
        coEvery { getProfessors(any()) } returns professorsMock
    }
    private val professorsViewModel = ProfessorsViewModel(scheduleController, professorsUseCase)

    /**
     * Complex test.
     */
    @Test
    fun complexTest() = runBlockingUnit {
        weekFlowMock.emit(lessonDataGenerator.getWeekMockk())
        professorsViewModel.state.collectPost(count = 2) {
            professorsViewModel.asyncSubscribe().joinWithTimeout()
            weekFlowMock.waitActiveSubscription()
        }.apply {
            assertEquals(2, this.size)
            assertEquals(emptyList<Professor>(), this[0]?.professors)
            assertEquals(professorsMock, this[1]?.professors)
        }
        professorsViewModel.unSubscribe()
    }

    /**
     * Complex test is empty.
     */
    @Test
    fun complexTest_isEmpty() = runBlockingUnit {
        professorsViewModel.state.collectPost {
            professorsViewModel.asyncSubscribe().joinWithTimeout()
        }.apply {
            assertEquals(2, this.size)
            assertEquals(emptyList<Professor>(), this[0]?.professors)
            assertEquals(emptyList<Professor>(), this[1]?.professors)
        }
        professorsViewModel.unSubscribe()
    }

    /**
     * Open faq screen.
     */
    @Test
    fun openFAQScreen() = runBlockingUnit {
        val actionJob = professorsViewModel.action.subscribeAndCompareFirstValue(ProfessorAction.OpenFAQScreen)
        professorsViewModel.dispatchIntentAsync(ProfessorIntent.OpenFAQScreen).joinWithTimeout()
        actionJob.joinWithTimeout()
    }

    /**
     * Open professor schedule screen.
     */
    @Test
    fun openProfessorScheduleScreen() = runBlockingUnit {
        val professorMock: Professor = mockk()
        val actionJob = professorsViewModel.action.subscribeAndCompareFirstValue(
            ProfessorAction.OpenProfessorScheduleScreen(professorMock)
        )
        professorsViewModel.dispatchIntentAsync(
            ProfessorIntent.OpenProfessorScheduleScreen(professorMock)
        ).joinWithTimeout()
        actionJob.joinWithTimeout()
    }

    /**
     * Open professor search screen.
     */
    @Test
    fun openProfessorSearchScreen() = runBlockingUnit {
        val actionJob = professorsViewModel.action.subscribeAndCompareFirstValue(
            ProfessorAction.OpenProfessorSearchScreen
        )
        professorsViewModel.dispatchIntentAsync(ProfessorIntent.OpenProfessorSearchScreen).joinWithTimeout()
        actionJob.joinWithTimeout()
    }
}