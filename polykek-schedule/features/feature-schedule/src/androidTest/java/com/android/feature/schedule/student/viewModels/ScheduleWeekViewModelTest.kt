package com.android.feature.schedule.student.viewModels

import com.android.common.models.schedule.Week
import com.android.schedule.controller.api.IScheduleController
import com.android.schedule.controller.api.IScheduleDateUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.getOrAwaitValue
import com.android.test.support.dataGenerator.LessonDataGenerator
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.testFixtures.waitActiveSubscription
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test
import java.util.*

/**
 * Schedule week view model test for [ScheduleWeekViewModel].
 *
 * @constructor Create empty constructor for schedule week view model test
 */
class ScheduleWeekViewModelTest : BaseViewModelUnitTest() {
    private val lessonDataGenerator = LessonDataGenerator()
    private val viewPagerPositionMock = 4
    private val weekFlowMockk = MutableStateFlow<Week?>(null)
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
     * Complex test.
     */
    @Test
    fun complexTest() = runBlockingUnit {
        val weekMockk = lessonDataGenerator.getWeekMockk()
        scheduleWeekViewModel.asyncSubscribe().joinWithTimeout()
        scheduleWeekViewModel.viewPagerPosition.getOrAwaitValue(Pair(viewPagerPositionMock, false))
        weekFlowMockk.waitActiveSubscription().emitAndWait(weekMockk).joinWithTimeout()
        scheduleWeekViewModel.weekTitle.getOrAwaitValue(weekMockk.title)
        scheduleWeekViewModel.dayTitleLiveData.getOrAwaitValue()
        scheduleWeekViewModel.getLessonsLiveData(0).getOrAwaitValue(expectedResult = weekMockk.days[0]!!.lessons)
        scheduleWeekViewModel.unSubscribe()
    }

    /**
     * Swipe day.
     */
    @Test
    fun swipeDay() = runBlockingUnit {
        scheduleWeekViewModel.swipeDay(0).joinWithTimeout()
        coVerify(exactly = 1) { scheduleDateUseCase.addToSelectedDate(Calendar.DATE, any()) }
        scheduleWeekViewModel.dayTitleLiveData.getOrAwaitValue()
    }

    /**
     * Show specific date.
     */
    @Test
    fun showSpecificDate() = runBlockingUnit {
        scheduleWeekViewModel.showSpecificDateAsync(2022, 10, 2).joinWithTimeout()
        coVerify(exactly = 1) {
            scheduleDateUseCase.setSelectedDay(any(), any(), any())
            scheduleController.updateSchedule(any())
            scheduleDateUseCase.getPeriod()
        }
        scheduleWeekViewModel.viewPagerPosition.getOrAwaitValue(Pair(viewPagerPositionMock, true))
    }

    /**
     * Show next week.
     */
    @Test
    fun showNextWeek() = runBlockingUnit {
        scheduleWeekViewModel.showNextWeekAsync().joinWithTimeout()
        verifyShiftWeek(1)
    }

    /**
     * Show previous week.
     */
    @Test
    fun showPreviousWeek() = runBlockingUnit {
        scheduleWeekViewModel.showPreviousWeekAsync().joinWithTimeout()
        verifyShiftWeek(-1)
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
        scheduleWeekViewModel.viewPagerPosition.getOrAwaitValue(Pair(viewPagerPositionMock, true))
    }
}