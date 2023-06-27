package com.android.feature.map.viewModels

import android.app.Application
import com.android.common.models.map.Building
import com.android.common.models.schedule.Lesson
import com.android.common.models.schedule.Week
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.feature.map.R
import com.android.feature.map.models.Content
import com.android.feature.map.models.MapInstructions
import com.android.feature.map.models.YandexMapItem
import com.android.feature.map.mvi.MapAction
import com.android.feature.map.mvi.MapIntent
import com.android.feature.map.mvi.MapState
import com.android.feature.map.useCases.DayControlsUseCase
import com.android.feature.map.useCases.MapUseCase
import com.android.schedule.controller.api.IScheduleController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

private const val DELAY_BEFORE_ANIMATION = 500L
private const val ONE_DAY = 1

/**
 * Map view model provides logic for Yandex-map screen.
 *
 * @property application Application object to get context
 * @property mapUseCase Yandex map use case is preparing instructions for Yandex-map
 * @property dayControlsUseCase Day controls use case
 * @property backgroundMessageBus Common background message bus with snack-bar output
 * @property scheduleController Schedule controller allows to synchronize UI changes between all tabs
 * @constructor Create [MapViewModel]
 */
internal class MapViewModel @Inject constructor(
    private val application: Application,
    private val mapUseCase: MapUseCase,
    private val dayControlsUseCase: DayControlsUseCase,
    @Named(BACKGROUND_MESSAGE_BUS) private val backgroundMessageBus: MutableSharedFlow<String>,
    private val scheduleController: IScheduleController
) : BaseSubscriptionViewModel<MapIntent, MapState, MapAction>(MapState.Default) {
    private var lastWeek: Week? = null
    private var mapInstructions: MapInstructions = MapInstructions()

    // Focus logic is specific -> actions should be executed outside of the main life circle.
    override val action = MutableSharedFlow<MapAction>(replay = 1)

    override suspend fun subscribe() {
        super.subscribe()
        subscribeToWeekFlow()
    }

    override suspend fun dispatchIntent(intent: MapIntent) {
        when (intent) {
            MapIntent.MakeDefaultFocus -> showDefaultFocus()
            MapIntent.ShowNextDay -> showNextDay()
            MapIntent.ShowPreviousDay -> showPreviousDay()
            MapIntent.ShowBuildingScreen -> MapAction.ShowBuildingScreen.emitAction()
            MapIntent.DeselectMapObject -> deselectMapObject()
            is MapIntent.SaveCameraPosition -> MapAction.UpdateCameraPosition(intent.cameraPosition).emitAction()
            is MapIntent.SelectMapObject -> selectMapObject(intent.userData)
            is MapIntent.BuildingSearchResult -> updateBuildingSearchMapItem(intent.building)
        }
    }

    /**
     * Subscribe to week flow.
     */
    private fun subscribeToWeekFlow() = scheduleController.weekFlow
        .onEach { week ->
            if (week != lastWeek) {
                this.lastWeek = week
                week?.updateMap()
            }
        }.cancelableLaunchInBackground()

    /**
     * Show default focus.
     */
    private suspend fun showDefaultFocus() = withContext(Dispatchers.Default) {
        currentState.copyState(content = null).emitState()
        MapAction.DefaultFocus(mapInstructions.boundingBox).emitAction()
    }

    /**
     * Show the next day.
     */
    private suspend fun showNextDay() = withContext(Dispatchers.Default) {
        scheduleController.indexOfDay += ONE_DAY
        lastWeek?.updateMap()
    }

    /**
     * Show the previous day.
     */
    private suspend fun showPreviousDay() = withContext(Dispatchers.Default) {
        scheduleController.indexOfDay -= ONE_DAY
        lastWeek?.updateMap()
    }

    /**
     * Deselect map object.
     */
    private suspend fun deselectMapObject() = withContext(Dispatchers.Default) {
        if (currentState.content != null) {
            currentState.copyState(content = null).emitState()
        }
    }

    /**
     * Select map object.
     *
     * @param userData User data
     */
    private suspend fun selectMapObject(userData: Any?) = withContext(Dispatchers.Default) {
        with(userData) {
            if (this is Lesson) {
                currentState.copyState(
                    content = Content(
                        title = title,
                        opposingTitle = time,
                        subTitle1 = typeLesson,
                        subTitle2 = teacherNames,
                        subTitle3 = address
                    )
                ).emitState()
            } else if (this is Building) {
                currentState.copyState(content = Content(name, address)).emitState()
            }
        }
    }

    /**
     * Update an additional yandex map item.
     *
     * @param building Building
     * @return [YandexMapItem] or null
     */
    private suspend fun updateBuildingSearchMapItem(building: Building?) = withContext(Dispatchers.Default) {
        building ?: return@withContext
        mapUseCase.getYandexMapItem(building)?.let { mapItem ->
            // Emit focus action immediately to avoid double focusing.
            MapAction.ShowBuildingSearchOnTheMap(mapItem.point).emitAction()
            // Show content after small delay - give pop-back animation time to finish before the new animation.
            delay(DELAY_BEFORE_ANIMATION)
            currentState.copyState(
                content = Content(building.name, building.address),
                searchResultMapItem = mapItem
            ).emitState()
        }
    }

    /**
     * Update map.
     *
     * @receiver [Week]
     */
    private suspend fun Week.updateMap() = withContext(Dispatchers.Default) {
        mapInstructions = mapUseCase.getMapInstruction(scheduleController.indexOfDay, this@updateMap)
        if (mapInstructions.isExistUnknownPlace) {
            backgroundMessageBus.emit(application.getString(R.string.map_fragment_one_or_more_addresses_are_not_known))
        }
        currentState.copyState(
            content = null,
            searchResultMapItem = null,
            yandexMapItems = mapInstructions.yandexMapItems,
            dayControls = dayControlsUseCase.getDayControls(scheduleController.indexOfDay)
        ).emitState()
        // Make default focus.
        MapAction.DefaultFocus(mapInstructions.boundingBox).emitAction()
    }
}