package com.android.feature.map.viewModels

import android.app.Application
import com.android.common.models.map.Building
import com.android.common.models.schedule.Lesson
import com.android.common.models.schedule.Week
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.feature.map.R
import com.android.feature.map.models.Content
import com.android.feature.map.models.DayControls
import com.android.feature.map.models.YandexMapItem
import com.android.feature.map.useCases.DayControlsUseCase
import com.android.feature.map.useCases.MapUseCase
import com.android.schedule.controller.api.IScheduleController
import com.android.shared.code.utils.liveData.EventLiveData
import com.yandex.mapkit.geometry.BoundingBox
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
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
) : BaseSubscriptionViewModel() {
    val selectedMapItem = EventLiveData<Content?>()
    val boundingBox = EventLiveData<BoundingBox>()
    val yandexMapItems = EventLiveData<List<YandexMapItem>>()
    val dayControls = EventLiveData<DayControls>()

    private var lastWeek: Week? = null

    override suspend fun subscribe() {
        super.subscribe()
        subscribeToWeekFlow()
    }

    /**
     * Subscribe to week flow.
     */
    private fun subscribeToWeekFlow() = scheduleController.weekFlow
        .onEach { week ->
            this.lastWeek = week
            week?.updateMap()
        }.cancelableLaunchInBackground()

    /**
     * Deselect map object. This null-checking help to avoid the multi deselecting and restart animation.
     */
    internal fun deselectMapObject() = selectedMapItem.value?.let {
        selectedMapItem.postValue(null)
    }

    /**
     * Focus map by bounding box.
     */
    internal fun refreshMapFocusByBoundingBox() = boundingBox.value?.apply {
        boundingBox.postValue(this)
    }

    /**
     * Select map object.
     *
     * @param userData User data
     */
    internal fun selectMapObject(userData: Any?) = launchInBackground {
        with(userData) {
            if (this is Lesson) {
                selectedMapItem.postValue(
                    Content(
                        title = title,
                        opposingTitle = time,
                        subTitle1 = typeLesson,
                        subTitle2 = teacherNames,
                        subTitle3 = address
                    )
                )
            } else if (this is Building) {
                delay(DELAY_BEFORE_ANIMATION)
                selectedMapItem.postValue(Content(name, address))
            }
        }
    }

    /**
     * Show the next day.
     */
    internal fun showNextDay() = launchInBackground {
        scheduleController.indexOfDay += ONE_DAY
        lastWeek?.updateMap()
        deselectMapObject()
    }

    /**
     * Show the previous day.
     */
    internal fun showPreviousDay() = launchInBackground {
        scheduleController.indexOfDay -= ONE_DAY
        lastWeek?.updateMap()
        deselectMapObject()
    }

    /**
     * Get yandex map item.
     *
     * @param building Building
     * @return [YandexMapItem] or null
     */
    internal fun getYandexMapItem(building: Building?) = mapUseCase.getYandexMapItem(building)

    /**
     * Update map.
     *
     * @receiver [Week]
     */
    private suspend fun Week.updateMap() {
        val mapInstructions = mapUseCase.getMapInstruction(scheduleController.indexOfDay, this)
        if (mapInstructions.isExistUnknownPlace) {
            backgroundMessageBus.emit(application.getString(R.string.map_fragment_one_or_more_addresses_are_not_known))
            mapInstructions.isExistUnknownPlace = false
        }
        yandexMapItems.postValue(mapInstructions.yandexMapItems)
        boundingBox.postValue(mapInstructions.boundingBox)
        dayControls.postValue(dayControlsUseCase.getDayControls(scheduleController.indexOfDay))
    }
}