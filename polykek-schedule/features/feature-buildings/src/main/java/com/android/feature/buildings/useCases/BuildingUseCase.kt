package com.android.feature.buildings.useCases

import com.android.common.models.map.Building
import com.android.core.retrofit.api.common.CatchResourceUseCase
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.feature.buildings.api.BuildingApiRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Building use case provides list of buildings fetched from Polytech server and sorted by keyword.
 *
 * @property buildingConvertUseCase Converts building response to building objects
 * @property buildingApiRepository Api to get list of Polytech buildings
 * @constructor Create [BuildingUseCase]
 *
 * @param backgroundMessageBus Common background message bus with snack-bar output
 */
internal class BuildingUseCase @Inject constructor(
    private val buildingConvertUseCase: BuildingConvertUseCase,
    private val buildingApiRepository: BuildingApiRepository,
    @Named(BACKGROUND_MESSAGE_BUS) backgroundMessageBus: MutableSharedFlow<String>
) : CatchResourceUseCase(backgroundMessageBus) {
    private var buildings: List<Building> = Collections.emptyList()
    private val mutex = Mutex()

    /**
     * Get buildings.
     *
     * @param keyWord Key word
     * @return Filtered buildings
     */
    internal suspend fun getBuildings(keyWord: String?): List<Building> = mutex.withLock {
        if (buildings.isEmpty()) {
            buildingApiRepository.getBuildings().catchRequestError {
                buildings = buildingConvertUseCase.convertResponseToBuildings(it)
            }
        }
        return buildingConvertUseCase.getFilteredBuildings(buildings, keyWord)
    }
}