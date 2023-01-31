package com.android.feature.schedule.professor.viewModels

import com.android.feature.schedule.base.viewModels.ONE_WEEK
import com.android.feature.schedule.professor.useCases.ProfessorScheduleUseCase
import com.android.schedule.controller.api.IScheduleDateUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.getOrAwaitValue
import com.android.test.support.dataGenerator.LessonDataGenerator
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeoutException

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

    // Mocks.
    private val professorScheduleUseCase: ProfessorScheduleUseCase = mockk {
        coEvery { getProfessorSchedule(any(), any()) } returns weekMockk
        coEvery { getRecyclerItems(any()) } returns emptyList()
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
     * Update professor schedule.
     */
    @Test
    fun updateProfessorSchedule() = runBlockingUnit {
        professorsScheduleViewModel.updateProfessorSchedule(professorIdMockk).joinWithTimeout()
        coVerify(exactly = 1) {
            scheduleDateUseCase.getPeriod()
            professorScheduleUseCase.getProfessorSchedule(professorIdMockk, periodMockk)
        }
        professorsScheduleViewModel.weekTitle.getOrAwaitValue(weekMockk.title)
        professorsScheduleViewModel.lessons.getOrAwaitValue(emptyList())
        professorsScheduleViewModel.listIsEmpty.getOrAwaitValue(true)
    }

    /**
     * Show next week.
     */
    @Test
    fun showNextWeek() = runBlockingUnit {
        professorsScheduleViewModel.showNextWeekAsync().joinWithTimeout()
        coVerify(exactly = 1) { scheduleDateUseCase.addToSelectedDate(Calendar.WEEK_OF_YEAR, ONE_WEEK) }
        professorsScheduleViewModel.isLoading.getOrAwaitValue() // Instead updateProfessorSchedule() checking.
    }

    /**
     * Show previous week.
     */
    @Test
    fun showPreviousWeek() = runBlockingUnit {
        professorsScheduleViewModel.showPreviousWeekAsync().joinWithTimeout()
        coVerify(exactly = 1) { scheduleDateUseCase.addToSelectedDate(Calendar.WEEK_OF_YEAR, -ONE_WEEK) }
        professorsScheduleViewModel.isLoading.getOrAwaitValue() // Instead updateProfessorSchedule() checking.
    }

    /**
     * Show specific date.
     */
    @Test
    fun showSpecificDate() = runBlockingUnit {
        professorsScheduleViewModel.showSpecificDateAsync(2020, 2, 3).joinWithTimeout()
        coVerify(exactly = 1) { scheduleDateUseCase.setSelectedDay(2020, 2, 3) }
        professorsScheduleViewModel.isLoading.getOrAwaitValue() // Instead updateProfessorSchedule() checking.
    }

    /**
     * Check and refresh.
     */
    @Test
    fun checkAndRefresh() = runBlockingUnit {
        professorsScheduleViewModel.checkAndRefresh().joinWithTimeout()
        coVerify(exactly = 1) { scheduleDateUseCase.getPeriod() }
        professorsScheduleViewModel.isLoading.getOrAwaitValue() // Instead updateProfessorSchedule() checking.
    }

    /**
     * Check and refresh request double call protection.*
     */
    @Test(expected = TimeoutException::class)
    fun checkAndRefresh_requestDoubleCallProtection() = runBlockingUnit {
        professorsScheduleViewModel.updateProfessorSchedule(professorIdMockk).joinWithTimeout()
        professorsScheduleViewModel.isLoading.getOrAwaitValue() // Instead updateProfessorSchedule() checking.
        professorsScheduleViewModel.checkAndRefresh().joinWithTimeout()
        professorsScheduleViewModel.isLoading.getOrAwaitValue() // Instead updateProfessorSchedule() checking.
    }
}