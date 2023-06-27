package com.android.feature.map.viewModels

import com.android.common.models.map.Building
import com.android.common.models.schedule.Week
import com.android.feature.map.models.Content
import com.android.feature.map.models.DayControls
import com.android.feature.map.models.MapInstructions
import com.android.feature.map.models.YandexMapItem
import com.android.feature.map.mvi.MapAction
import com.android.feature.map.mvi.MapIntent
import com.android.feature.map.mvi.MapState
import com.android.feature.map.useCases.DayControlsUseCase
import com.android.feature.map.useCases.MapUseCase
import com.android.schedule.controller.api.IScheduleController
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.dataGenerator.LessonDataGenerator
import com.android.test.support.testFixtures.ONE_SECOND
import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.testFixtures.waitActiveSubscription
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import io.mockk.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
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
    private val building = Building(name = "Super name", nameWithAbbr = "SN", address = "Super address")

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
        val defaultFocus = MapAction.DefaultFocus(mapInstructions.boundingBox)
        val singleActionsJob = mapViewModel.action.subscribeAndCompareFirstValue(defaultFocus)

        mapViewModel.asyncSubscribe().joinWithTimeout()
        weekFlowMock.waitActiveSubscription().emitAndWait(weekMockk).joinWithTimeout()
        backgroundMessageBus.subscribeAndCompareFirstValue(TEST_STRING).joinWithTimeout()
        coVerify(exactly = 1, timeout = ONE_SECOND) {
            mapUseCase.getMapInstruction(0, weekMockk)
            dayControlsUseCase.getDayControls(0)
        }
        assertEquals(
            MapState.Update(
                content = null,
                searchResultMapItem = null,
                yandexMapItems = mapInstructions.yandexMapItems,
                dayControls = dayControls
            ),
            mapViewModel.state.value
        )
        singleActionsJob.joinWithTimeout()
        mapViewModel.unSubscribe()
    }

    /**
     * Focus map by bounding box.
     */
    @Test
    fun showDefaultFocus() = runBlockingUnit {
        val defaultFocus = MapAction.DefaultFocus(mapInstructions.boundingBox)
        val singleActionsJob = mapViewModel.action.subscribeAndCompareFirstValue(defaultFocus)

        mapViewModel.asyncSubscribe().joinWithTimeout()
        weekFlowMock.waitActiveSubscription().emitAndWait(weekMockk).joinWithTimeout()
        mapViewModel.dispatchIntentAsync(MapIntent.MakeDefaultFocus).joinWithTimeout()
        assertEquals(
            MapState.Update(
                content = null,
                searchResultMapItem = null,
                yandexMapItems = mapInstructions.yandexMapItems,
                dayControls = null
            ),
            mapViewModel.state.value
        )
        singleActionsJob.joinWithTimeout()
        mapViewModel.unSubscribe()
    }

    /**
     * Show next day.
     */
    @Test
    fun showNextDay() = runBlockingUnit {
        mapViewModel.dispatchIntentAsync(MapIntent.ShowNextDay).joinWithTimeout()
        coVerify(exactly = 1) { scheduleController setProperty "indexOfDay" value 1 }
    }

    /**
     * Show previous day.
     */
    @Test
    fun showPreviousDay() = runBlockingUnit {
        mapViewModel.dispatchIntentAsync(MapIntent.ShowPreviousDay).joinWithTimeout()
        coVerify(exactly = 1) { scheduleController setProperty "indexOfDay" value -1 }
    }

    /**
     * Show building screen.
     */
    @Test
    fun showBuildingScreen() = runBlockingUnit {
        val singleActionsJob = mapViewModel.action.subscribeAndCompareFirstValue(MapAction.ShowBuildingScreen)
        mapViewModel.dispatchIntentAsync(MapIntent.ShowBuildingScreen).joinWithTimeout()
        singleActionsJob.joinWithTimeout()
    }

    /**
     * Select and deselect with lesson-user-data.
     */
    @Test
    fun selectAndDeselectMapObjectWithLesson() = runBlockingUnit {
        val lesson = lessonDataGenerator.getLessonMockk()
        mapViewModel.state.collectPost {
            mapViewModel.dispatchIntentAsync(MapIntent.SelectMapObject(lesson)).joinWithTimeout()
            mapViewModel.dispatchIntentAsync(MapIntent.DeselectMapObject).joinWithTimeout()
        }.apply {
            assertEquals(3, size)
            assertEquals(MapState.Default, this[0])
            assertEquals(
                MapState.Update(
                    content = Content(
                        title = lesson.title,
                        opposingTitle = lesson.time,
                        subTitle1 = lesson.typeLesson,
                        subTitle2 = lesson.teacherNames,
                        subTitle3 = lesson.address
                    ),
                    searchResultMapItem = null,
                    yandexMapItems = mapInstructions.yandexMapItems,
                    dayControls = null
                ),
                this[1]
            )
            assertEquals(
                MapState.Update(
                    content = null,
                    searchResultMapItem = null,
                    yandexMapItems = mapInstructions.yandexMapItems,
                    dayControls = null
                ),
                this[2]
            )
        }
    }

    /**
     * Select and deselect with building-data.
     */
    @Test
    fun selectAndDeselectMapObjectWithBuilding() = runBlockingUnit {
        mapViewModel.state.collectPost {
            mapViewModel.dispatchIntentAsync(MapIntent.SelectMapObject(building)).joinWithTimeout()
            mapViewModel.dispatchIntentAsync(MapIntent.DeselectMapObject).joinWithTimeout()
        }.apply {
            assertEquals(3, size)
            assertEquals(MapState.Default, this[0])
            assertEquals(
                MapState.Update(
                    content = Content(building.name, building.address),
                    searchResultMapItem = null,
                    yandexMapItems = mapInstructions.yandexMapItems,
                    dayControls = null
                ),
                this[1]
            )
            assertEquals(
                MapState.Update(
                    content = null,
                    searchResultMapItem = null,
                    yandexMapItems = mapInstructions.yandexMapItems,
                    dayControls = null
                ),
                this[2]
            )
        }
    }

    /**
     * Save camera position.
     */
    @Test
    fun saveCameraPosition() = runBlockingUnit {
        val cameraPosition = CameraPosition()
        val expectedAction = MapAction.UpdateCameraPosition(cameraPosition)
        val singleActionsJob = mapViewModel.action.subscribeAndCompareFirstValue(expectedAction)
        mapViewModel.dispatchIntentAsync(MapIntent.SaveCameraPosition(cameraPosition)).joinWithTimeout()
        singleActionsJob.joinWithTimeout()
    }

    /**
     * Building search result.
     */
    @Test
    fun buildingSearchResult() = runBlockingUnit {
        val mapItemMock = YandexMapItem(point = Point(0.0, 0.0), imageId = 0, userData = null)
        coEvery { mapUseCase.getYandexMapItem(any()) } returns mapItemMock

        val expectedAction = MapAction.ShowBuildingSearchOnTheMap(mapItemMock.point)
        val singleActionsJob = mapViewModel.action.subscribeAndCompareFirstValue(expectedAction)
        mapViewModel.dispatchIntentAsync(MapIntent.BuildingSearchResult(building)).joinWithTimeout()
        coVerify(exactly = 1) { mapUseCase.getYandexMapItem(any()) }
        singleActionsJob.joinWithTimeout()
        assertEquals(
            MapState.Update(
                content = Content(building.name, building.address),
                searchResultMapItem = mapItemMock,
                yandexMapItems = mapInstructions.yandexMapItems,
                dayControls = null
            ),
            mapViewModel.state.value
        )
    }
}