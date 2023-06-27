package com.android.feature.buildings.useCases

import com.android.common.models.map.Building
import com.android.feature.buildings.api.BuildingsResponse
import com.android.shared.code.utils.markers.IUseCase
import javax.inject.Inject

/**
 * Converts building response to building objects.
 *
 * @constructor Create [BuildingConvertUseCase]
 */
internal class BuildingConvertUseCase @Inject constructor() : IUseCase {
    private val blackList = listOf("DL", "Выездное", "Не определено", "Литейный корпус")

    /**
     * Convert [BuildingsResponse] to [Building] list.
     *
     * @param buildingsResponse [BuildingsResponse]
     * @return Converted [Building]
     */
    internal fun convertResponseToBuildings(buildingsResponse: BuildingsResponse): List<Building> = ArrayList<Building>().apply {
        buildingsResponse.buildingResponses.forEach { buildingResponse ->
            if (!blackList.contains(buildingResponse.name) && buildingResponse.name != null) {
                with(buildingResponse) {
                    Building(
                        name = name!!,
                        nameWithAbbr = if (name == abbr) name!! else "$name ($abbr)",
                        address = address
                    ).let { building ->
                        this@apply.add(building)
                    }
                }
            }
        }
    }

    /**
     * Get filtered buildings.
     *
     * @param buildings List of [Building]
     * @param keyWord Key word
     * @return Filtered by keyword buildings
     */
    fun getFilteredBuildings(buildings: List<Building>, keyWord: String?): List<Building> = if (!keyWord.isNullOrEmpty()) {
        buildings.filter {
            it.nameWithAbbr.contains(keyWord, ignoreCase = true) ||
                    it.address?.contains(keyWord, ignoreCase = true) ?: false
        }
    } else {
        buildings
    }
}