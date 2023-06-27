package com.android.feature.buildings.viewModels

import com.android.common.models.map.Building
import com.android.feature.buildings.mvi.BuildingAction
import com.android.feature.buildings.mvi.BuildingIntent
import com.android.feature.buildings.mvi.BuildingState
import com.android.feature.buildings.useCases.BuildingUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Building view model test for [BuildingViewModel].
 *
 * @constructor Create empty constructor for building view model test
 */
class BuildingViewModelTest : BaseViewModelUnitTest() {
    private val buildingsMocks = listOf(
        Building(
            name = "6-й учебный корпус",
            nameWithAbbr = "6-й учебный корпус (6 к.)",
            address = ""
        )
    )
    private val buildingUseCase: BuildingUseCase = mockk()
    private val buildingViewModel = BuildingViewModel(buildingUseCase)

    /**
     * Check [BuildingIntent.LoadContent] and [BuildingIntent.KeyWordChanged].
     */
    @Test
    fun complex() = runBlockingUnit {
        coEvery { buildingUseCase.getBuildings(any()) } returns buildingsMocks
        buildingViewModel.state.collectPost {
            buildingViewModel.dispatchIntentAsync(BuildingIntent.LoadContent).joinWithTimeout()
            buildingViewModel.dispatchIntentAsync(BuildingIntent.KeyWordChanged(keyWord = "abc")).joinWithTimeout()
        }.apply {
            assertEquals(5, this.size)
            this[0].checkState(isLoading = false, buildings = emptyList(), keyWord = null)      // Default
            this[1].checkState(isLoading = true, buildings = emptyList(), keyWord = null)       // LoadContent.Loading
            this[2].checkState(isLoading = false, buildings = buildingsMocks, keyWord = null)   // LoadContent.Loaded
            this[3].checkState(isLoading = true, buildings = buildingsMocks, keyWord = "abc")   // ChangeKeyWord.Loading
            this[4].checkState(isLoading = false, buildings = buildingsMocks, keyWord = "abc")  // ChangeKeyWord.Loaded
        }
    }

    /**
     * Check state.
     *
     * @receiver [BuildingState] or null
     * @param isLoading Is loading
     * @param buildings Buildings
     * @param keyWord Key word
     */
    private fun BuildingState?.checkState(isLoading: Boolean, buildings: List<Building>, keyWord: String?) {
        this ?: throw AssertionError("State is null")
        assertEquals(isLoading, this.isLoading)
        assertEquals(buildings, this.buildings)
        assertEquals(keyWord, this.keyWord)
    }

    /**
     * Check navigation by selected building.
     */
    @Test
    fun complex2() = runBlocking {
        coEvery { buildingUseCase.getBuildings(any()) } returns buildingsMocks
        val selectedBuilding = BuildingIntent.BuildingSelected(buildingsMocks.first())
        val selectBuildingJob = buildingViewModel.action.subscribeAndCompareFirstValue(
            BuildingAction.SelectBuilding(selectedBuilding.building))
        buildingViewModel.dispatchIntentAsync(selectedBuilding).joinWithTimeout()
        selectBuildingJob.joinWithTimeout()
    }
}