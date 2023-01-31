package com.android.feature.map.viewModels

import com.android.common.models.schedule.Week
import com.android.feature.map.models.Content
import com.android.feature.map.models.DayControls
import com.android.feature.map.models.MapInstructions
import com.android.feature.map.useCases.DayControlsUseCase
import com.android.feature.map.useCases.MapUseCase
import com.android.schedule.controller.api.IScheduleController
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.collectPost
import com.android.test.support.androidTest.utils.getOrAwaitValue
import com.android.test.support.dataGenerator.LessonDataGenerator
import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.testFixtures.waitActiveSubscription
import com.yandex.mapkit.geometry.BoundingBox
import io.mockk.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * Map view model test for [MapViewModel].
 *
 * @constructor Create empty constructor for map view model test
 */
class MapViewModelTest : BaseViewModelUnitTest() {
    private val lessonDataGenerator = LessonDataGenerator()

    // Data.
    private val weekFlowMock = MutableStateFlow<Week?>(null)
    private val weekMockk = lessonDataGenerator.getWeekMockk()
    private val mapInstructions = MapInstructions(emptyList(), true, BoundingBox())
    private val dayControls: DayControls = mockk()

    // Classes.
    private val backgroundMessageBus = MutableSharedFlow<String>()
    private val mapUseCase: MapUseCase = mockk {
        coEvery { getMapInstruction(any(), any()) } returns mapInstructions
    }
    private val dayControlsUseCase: DayControlsUseCase = mockk {
        coEvery { getDayControls(any()) } returns dayControls
    }
    private val scheduleController: IScheduleController = mockk {
        coEvery { indexOfDay } returns 0
        coEvery { weekFlow } returns weekFlowMock
    }
    private val mapViewModel = MapViewModel(
        application = application,
        mapUseCase = mapUseCase,
        dayControlsUseCase = dayControlsUseCase,
        backgroundMessageBus = backgroundMessageBus,
        scheduleController = scheduleController
    )

    override fun beforeTest() {
        super.beforeTest()
        coEvery { scheduleController setProperty "indexOfDay" value any<Int>() } just runs
    }

    /**
     * Complex test.
     */
    @Test
    fun complexTest() = runBlockingUnit {
        mapViewModel.asyncSubscribe().joinWithTimeout()
        weekFlowMock.waitActiveSubscription().emitAndWait(weekMockk).joinWithTimeout()
        backgroundMessageBus.subscribeAndCompareFirstValue(TEST_STRING).joinWithTimeout()
        coVerify(exactly = 1) { mapUseCase.getMapInstruction(0, weekMockk) }
        mapViewModel.yandexMapItems.getOrAwaitValue(mapInstructions.yandexMapItems)
        mapViewModel.boundingBox.getOrAwaitValue(mapInstructions.boundingBox)
        mapViewModel.dayControls.getOrAwaitValue(dayControls)
        coVerify(exactly = 1) { dayControlsUseCase.getDayControls(0) }
        assertFalse(mapInstructions.isExistUnknownPlace)
        mapViewModel.unSubscribe()
    }

    /**
     * Focus map by bounding box.
     */
    @Test
    fun focusMapByBoundingBox() = runBlockingUnit {
        mapViewModel.asyncSubscribe().joinWithTimeout()
        weekFlowMock.waitActiveSubscription().emitAndWait(weekMockk).joinWithTimeout()
        mapViewModel.boundingBox.getOrAwaitValue(mapInstructions.boundingBox)
        mapViewModel.refreshMapFocusByBoundingBox()
        mapViewModel.boundingBox.getOrAwaitValue(mapInstructions.boundingBox)
        mapViewModel.unSubscribe()
    }

    /**
     * Select and deselect.
     */
    @Test
    fun selectAndDeselect() = runBlockingUnit {
        val lesson = lessonDataGenerator.getLessonMockk()
        mapViewModel.selectedMapItem.collectPost {
            mapViewModel.asyncSubscribe().joinWithTimeout()
            mapViewModel.selectMapObject(lesson).joinWithTimeout()
            mapViewModel.deselectMapObject()
        }.apply {
            assertEquals(2, size)
            assertEquals(
                Content(
                    title = lesson.title,
                    opposingTitle = lesson.time,
                    subTitle1 = lesson.typeLesson,
                    subTitle2 = lesson.teacherNames,
                    subTitle3 = lesson.address
                ), first()
            )
            assertEquals(null, last())
        }
        mapViewModel.unSubscribe()
    }

    /**
     * Show previous day.
     */
    @Test
    fun showPreviousDay() = runBlockingUnit {
        mapViewModel.showPreviousDay().joinWithTimeout()
        coVerify(exactly = 1) { scheduleController setProperty "indexOfDay" value -1 }
    }

    /**
     * Show next day.
     */
    @Test
    fun showNextDay() = runBlockingUnit {
        mapViewModel.showNextDay().joinWithTimeout()
        coVerify(exactly = 1) { scheduleController setProperty "indexOfDay" value 1 }
    }

    /**
     * Get yandex map item.
     */
    @Test
    fun getYandexMapItem() {
        coEvery { mapUseCase.getYandexMapItem(any()) } returns mockk()
        mapViewModel.getYandexMapItem(mockk())
        coVerify(exactly = 1) { mapUseCase.getYandexMapItem(any()) }
    }
}