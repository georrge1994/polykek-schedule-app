package com.android.feature.schedule.student.viewModels

import com.android.common.models.schedule.Week
import com.android.common.models.schedule.stubWeek
import com.android.feature.schedule.student.mvi.StudentAction
import com.android.feature.schedule.student.mvi.StudentIntent
import com.android.feature.schedule.student.mvi.StudentState
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.IScheduleDateUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.dataGenerator.LessonDataGenerator
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.testFixtures.waitActiveSubscription
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test
import java.util.Calendar

/**
 * Schedule week view model test for [ScheduleWeekViewModel].
 *
 * @constructor Create empty constructor for schedule week view model test
 */
class ScheduleWeekViewModelTest : BaseViewModelUnitTest() {
    private val lessonDataGenerator = LessonDataGenerator()
    private val viewPagerPositionMock = 4
    private val weekFlowMockk = MutableStateFlow<Week?>(stubWeek)
    private val scheduleController: IScheduleController = mockk {
        coEvery { weekFlow } returns weekFlowMockk
        coEvery { updateSchedule(any()) } returns Unit
    }
    private val scheduleDateUseCase: IScheduleDateUseCase = mockk {
        coEvery { getCurrentDay() } returns Calendar.getInstance().time
        coEvery { selectedDate } returns Calendar.getInstance()
        coEvery { addToSelectedDate(any(), any()) } returns Unit
        coEvery { setSelectedDay(any(), any(), any()) } returns Unit
        coEvery { getPeriod() } returns "10.10.2022"
    }
    private lateinit var scheduleWeekViewModel: ScheduleWeekViewModel

    override fun beforeTest() {
        super.beforeTest()
        coEvery { scheduleController setProperty "indexOfDay" value any<Int>() } just runs
        coEvery { scheduleController getProperty "indexOfDay" } returns viewPagerPositionMock
        scheduleWeekViewModel = ScheduleWeekViewModel(
            application = application,
            scheduleController = scheduleController,
            scheduleDateUseCase = scheduleDateUseCase,
        )
        coVerify {
            scheduleDateUseCase.getCurrentDay()
            scheduleDateUseCase.selectedDate
        }
    }

    /**
     * Complex test. TODO: fix this test in the future.
     */
    @Test
    fun complexTest() = runBlockingUnit {
        val weekMockk = lessonDataGenerator.getWeekMockk()
        scheduleWeekViewModel.state.collectPost(count = 3) {
            scheduleWeekViewModel.asyncSubscribe().joinWithTimeout()
            weekFlowMockk.waitActiveSubscription().emitAndWait(weekMockk).joinWithTimeout()
        }.apply {
            assertEquals(StudentState.Default, this[0])
            assertEquals(StudentState.Update(
                weekTitle = "...",
                dayTitle = "",
                days = emptyMap(),
                viewPagerPosition = viewPagerPositionMock,
                smoothPaging = false,
                isLoading = true
            ), this[1])
            assertEquals(StudentState.Update(
                weekTitle = weekMockk.title,
                dayTitle = this[2]?.dayTitle ?: "", // Don't check this property.
                days = weekMockk.days,
                viewPagerPosition = viewPagerPositionMock,
                smoothPaging = false,
                isLoading = false
            ), this[2])
        }
        scheduleWeekViewModel.unSubscribe()
    }

    /**
     * Open date picker for selected date.
     */
    @Test
    fun openDatePickerForSelectedDate() = runBlockingUnit {
        val date = Calendar.getInstance()
        coEvery { scheduleDateUseCase.selectedDate } returns date
        val expectedAction = StudentAction.OpenDatePicker(
            day = date.get(Calendar.DATE),
            month = date.get(Calendar.MONTH),
            year = date.get(Calendar.YEAR)
        )
        val actionJob = scheduleWeekViewModel.action.subscribeAndCompareFirstValue(expectedAction)
        scheduleWeekViewModel.dispatchIntentAsync(StudentIntent.ShowDataPicker).joinWithTimeout()
        actionJob.joinWithTimeout()
    }

    /**
     * Swipe day.
     */
    @Test
    fun swipeDay() = runBlockingUnit {
        scheduleWeekViewModel.dispatchIntentAsync(StudentIntent.SwipeDay(0)).joinWithTimeout()
        coVerify(exactly = 1) { scheduleDateUseCase.addToSelectedDate(Calendar.DATE, any()) }
        coVerify(exactly = 2) { scheduleController setProperty "indexOfDay" value any<Int>() }
        scheduleWeekViewModel.currentState.apply {
            assertEquals(viewPagerPositionMock, viewPagerPosition) // Notes! This is not 0, because mocked logic.
            assertEquals(false, smoothPaging)
            assertEquals(true, isLoading)
        }
    }

    /**
     * Show specific date.
     */
    @Test
    fun showSpecificDate() = runBlockingUnit {
        scheduleWeekViewModel.dispatchIntentAsync(
            StudentIntent.ShowSpecificDate(2022, 10, 2)).joinWithTimeout()
        coVerify(exactly = 1) {
            scheduleDateUseCase.setSelectedDay(any(), any(), any())
            scheduleController.updateSchedule(any())
            scheduleDateUseCase.getPeriod()
        }
        coVerify(exactly = 2) { scheduleController setProperty "indexOfDay" value any<Int>() }
        scheduleWeekViewModel.currentState.apply {
            assertEquals(viewPagerPositionMock, viewPagerPosition)
            assertEquals(true, smoothPaging)
            assertEquals(true, isLoading)
        }
    }

    /**
     * Show next week.
     */
    @Test
    fun showNextWeek() = runBlockingUnit {
        scheduleWeekViewModel.dispatchIntentAsync(StudentIntent.ShowNextWeek).joinWithTimeout()
        verifyShiftWeek(1)
    }

    /**
     * Show previous week.
     */
    @Test
    fun showPreviousWeek() = runBlockingUnit {
        scheduleWeekViewModel.dispatchIntentAsync(StudentIntent.ShowPreviousWeek).joinWithTimeout()
        verifyShiftWeek(-1)
    }

    /**
     * Open note editor.
     */
    @Test
    fun openNoteEditor() = runBlockingUnit {
        val dayIdMockk = 1
        val noteIdMockk = "NoteId"
        val titleMockk = "title"
        val actionJob = scheduleWeekViewModel.action.subscribeAndCompareFirstValue(
            StudentAction.OpenNoteEditor(dayIdMockk, noteIdMockk, titleMockk)
        )
        scheduleWeekViewModel.dispatchIntentAsync(
            StudentIntent.OpenNoteEditor(dayIdMockk, noteIdMockk, titleMockk)).joinWithTimeout()
        actionJob.joinWithTimeout()
    }

    /**
     * Verify shift week.
     *
     * @param weekNumber Week number
     */
    private suspend fun verifyShiftWeek(weekNumber: Int) {
        coVerify(exactly = 1) {
            scheduleDateUseCase.addToSelectedDate(Calendar.WEEK_OF_YEAR, weekNumber)
            scheduleDateUseCase.addToSelectedDate(Calendar.DATE, any())
            scheduleController.updateSchedule(any())
        }
        scheduleWeekViewModel.currentState.apply {
            assertEquals(viewPagerPositionMock, viewPagerPosition)
            assertEquals(true, smoothPaging)
            assertEquals(true, isLoading)
        }
    }
}