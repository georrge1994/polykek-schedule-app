package com.android.feature.buildings.useCases

import com.android.common.models.map.Building
import com.android.common.models.map.BuildingResponse
import com.android.feature.buildings.api.BuildingsResponse
import com.android.test.support.unitTest.BaseUnitTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Building convert use case test for [BuildingConvertUseCase].
 *
 * @constructor Create empty constructor for building convert use case test
 */
class BuildingConvertUseCaseTest : BaseUnitTest() {
    private val buildingConvertUseCase = BuildingConvertUseCase()

    @Test
    fun convertResponseToBuildings() = runTest {
        val buildingResponse = BuildingsResponse(
            listOf(
                BuildingResponse(
                    id = 1,
                    name = "6-й учебный корпус",
                    abbr = "6 к.",
                    address = ""
                ),
                // Name from black list.
                BuildingResponse(
                    id = 2,
                    name = "DL",
                    abbr = "6 к.",
                    address = ""
                )
            )
        )
        assertEquals(
            listOf(
                Building(
                    name = "6-й учебный корпус",
                    nameWithAbbr = "6-й учебный корпус (6 к.)",
                    address = ""
                )
            ),
            buildingConvertUseCase.convertResponseToBuildings(buildingResponse)
        )
    }

    /**
     * Get filtered buildings.
     */
    @Test
    fun getFilteredBuildings() = runTest {
        val keyWord = "6"
        val buildings = listOf(
            Building(
                name = "6-й учебный корпус",
                nameWithAbbr = "6-й учебный корпус (6 к.)",
                address = ""
            ),
            Building(
                name = "7-й учебный корпус",
                nameWithAbbr = "7-й учебный корпус (7 к.)",
                address = ""
            )
        )
        assertEquals(
            buildings.subList(0, 1),
            buildingConvertUseCase.getFilteredBuildings(buildings, keyWord)
        )
    }

    /**
     * Get filtered buildings 2.
     */
    @Test
    fun getFilteredBuildings2() = runTest {
        val keyWord = "8"
        val buildings = listOf(
            Building(
                name = "6-й учебный корпус",
                nameWithAbbr = "6-й учебный корпус (6 к.)",
                address = ""
            ),
            Building(
                name = "7-й учебный корпус",
                nameWithAbbr = "7-й учебный корпус (7 к.)",
                address = ""
            )
        )
        assertEquals(
            emptyList<Building>(),
            buildingConvertUseCase.getFilteredBuildings(buildings, keyWord)
        )
    }
}