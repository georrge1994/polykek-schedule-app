package com.android.feature.schedule.professor.viewModels

import com.android.feature.schedule.professor.mvi.ProfessorAction
import com.android.feature.schedule.professor.mvi.ProfessorIntent
import com.android.feature.schedule.professor.mvi.ProfessorState
import com.android.feature.schedule.professor.useCases.ProfessorScheduleUseCase
import com.android.schedule.controller.api.IScheduleDateUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.dataGenerator.LessonDataGenerator
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

/**
 * Professors schedule view model test for [ProfessorsScheduleViewModel].
 *
 * @constructor Create empty constructor for professors schedule view model test
 */
class ProfessorsScheduleViewModelTest : BaseViewModelUnitTest() {
    // Data.
    private val lessonDataGenerator = LessonDataGenerator()
    private val weekMockk = lessonDataGenerator.getWeekMockk()
    private val professorIdMockk = 1
    private val periodMockk = "01.01.2022"
    private val listMockk = listOf(Any())

    // Mocks.
    private val professorScheduleUseCase: ProfessorScheduleUseCase = mockk {
        coEvery { getProfessorSchedule(any(), any()) } returns weekMockk
        coEvery { getRecyclerItems(any()) } returns listMockk
    }
    private val scheduleDateUseCase: IScheduleDateUseCase = mockk {
        coEvery { getPeriod() } returns periodMockk
        coEvery { addToSelectedDate(any(), any()) } returns Unit
        coEvery { setSelectedDay(any(), any(), any()) } returns Unit
    }
    private val professorsScheduleViewModel = ProfessorsScheduleViewModel(
        application = application,
        professorScheduleUseCase = professorScheduleUseCase,
        scheduleDateUseCase = scheduleDateUseCase
    )

    /**
     * Check and refresh.
     */
    @Test
    fun checkAndRefresh() = runBlockingUnit {
        setProfessorIdAndShortCheckUpdateProfessorSchedule()
        professorsScheduleViewModel.dispatchIntentAsync(ProfessorIntent.CheckPeriodAndRefresh).joinWithTimeout()
        coVerify(exactly = 2) { scheduleDateUseCase.getPeriod() } // Instead updateProfessorSchedule() checking.
    }

    /**
     * Check and refresh request double call protection.*
     */
    @Test
    fun checkAndRefresh_requestDoubleCallProtection() = runBlockingUnit {
        setProfessorIdAndShortCheckUpdateProfessorSchedule()
        // Try to refresh the same period. It will be ignored, so we expect only one call.
        professorsScheduleViewModel.state.collectPost {
            professorsScheduleViewModel.dispatchIntentAsync(ProfessorIntent.CheckPeriodAndRefresh).joinWithTimeout()
        }.apply {
            assert(this.size == 1)
        }
    }

    /**
     * Open date picker for selected date.
     */
    @Test
    fun openDatePickerForSelectedDate() = runBlockingUnit {
        val date = Calendar.getInstance()
        coEvery { scheduleDateUseCase.selectedDate } returns date
        val expectedAction = ProfessorAction.OpenDatePicker(
            day = date.get(Calendar.DATE),
            month = date.get(Calendar.MONTH),
            year = date.get(Calendar.YEAR)
        )
        val actionJob = professorsScheduleViewModel.action.subscribeAndCompareFirstValue(expectedAction)
        professorsScheduleViewModel.dispatchIntentAsync(ProfessorIntent.ShowDataPicker).joinWithTimeout()
        actionJob.joinWithTimeout()
    }

    /**
     * Show next week.
     */
    @Test
    fun showNextWeek() = runBlockingUnit {
        setProfessorIdAndShortCheckUpdateProfessorSchedule()
        professorsScheduleViewModel.dispatchIntentAsync(ProfessorIntent.ShowNextWeek).joinWithTimeout()
        coVerify(exactly = 1) { scheduleDateUseCase.addToSelectedDate(Calendar.WEEK_OF_YEAR, 1) }
        coVerify(exactly = 2) { scheduleDateUseCase.getPeriod() } // Instead updateProfessorSchedule() checking.
    }

    /**
     * Show previous week.
     */
    @Test
    fun showPreviousWeek() = runBlockingUnit {
        setProfessorIdAndShortCheckUpdateProfessorSchedule()
        professorsScheduleViewModel.dispatchIntentAsync(ProfessorIntent.ShowPreviousWeek).joinWithTimeout()
        coVerify(exactly = 1) { scheduleDateUseCase.addToSelectedDate(Calendar.WEEK_OF_YEAR, -1) }
        coVerify(exactly = 2) { scheduleDateUseCase.getPeriod() } // Instead updateProfessorSchedule() checking.
    }

    /**
     * Show specific date.
     */
    @Test
    fun showSpecificDate() = runBlockingUnit {
        setProfessorIdAndShortCheckUpdateProfessorSchedule()
        professorsScheduleViewModel.dispatchIntentAsync(
            ProfessorIntent.ShowSpecificDate(2020, 2, 3)).joinWithTimeout()
        coVerify(exactly = 1) { scheduleDateUseCase.setSelectedDay(2020, 2, 3) }
        coVerify(exactly = 2) { scheduleDateUseCase.getPeriod() } // Instead updateProfessorSchedule() checking.
    }

    /**
     * Complex test of update professor schedule.
     */
    @Test
    fun updateProfessorSchedule() = runBlockingUnit {
        professorsScheduleViewModel.state.collectPost {
            professorsScheduleViewModel.dispatchIntentAsync(
                ProfessorIntent.UpdateProfessorId(professorIdMockk)).joinWithTimeout()
            coVerify(exactly = 1) {
                scheduleDateUseCase.getPeriod()
                professorScheduleUseCase.getProfessorSchedule(professorIdMockk, periodMockk)
                professorScheduleUseCase.getRecyclerItems(weekMockk)
            }
        }.apply {
            assertEquals(3, this.size)
            assertEquals(ProfessorState.Default, this[0])
            assertEquals(ProfessorState.Update(
                weekTitle = "",
                lessonsAndHeaders = emptyList(),
                isLoading = true
            ), this[1])
            assertEquals(ProfessorState.Update(
                weekTitle = weekMockk.title,
                lessonsAndHeaders = listMockk,
                isLoading = false
            ), this[2])
        }
    }

    /**
     * Set professor id for checking call "updateProfessorSchedule".
     */
    private suspend fun setProfessorIdAndShortCheckUpdateProfessorSchedule() {
        professorsScheduleViewModel.dispatchIntentAsync(
            ProfessorIntent.UpdateProfessorId(professorIdMockk)).joinWithTimeout()
        coVerify(exactly = 1) { scheduleDateUseCase.getPeriod() } // Instead updateProfessorSchedule() checking.
    }
}