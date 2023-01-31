package com.android.feature.buildings.viewModels

import com.android.common.models.map.Building
import com.android.feature.buildings.useCases.BuildingUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.collectPost
import com.android.test.support.androidTest.utils.getOrAwaitValue
import com.android.test.support.testFixtures.joinWithTimeout
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
    private val buildings = listOf(
        Building(
            name = "6-й учебный корпус",
            nameWithAbbr = "6-й учебный корпус (6 к.)",
            address = ""
        )
    )
    private val buildingUseCase: BuildingUseCase = mockk()
    private val buildingViewModel = BuildingViewModel(buildingUseCase)

    /**
     * Complex: data is NOT empty. Check all observers.
     */
    @Test
    fun complex() = runBlocking {
        coEvery { buildingUseCase.getBuildings(any()) } returns buildings
        buildingViewModel.isLoading.collectPost {
            buildingViewModel.asyncSubscribe().joinWithTimeout()
            buildingViewModel.updateKeyWordAsync("new word").joinWithTimeout()
            buildingViewModel.buildings.getOrAwaitValue(buildings)
            buildingViewModel.isListEmpty.getOrAwaitValue(false)
        }.apply {
            assertEquals(2, this.size)
            assertEquals(true, first())
            assertEquals(false, last())
        }
        buildingViewModel.unSubscribe()
    }

    /**
     * Complex 2: data is empty. Check all observers.
     */
    @Test
    fun complex2() = runBlocking {
        coEvery { buildingUseCase.getBuildings(any()) } returns emptyList()
        buildingViewModel.isLoading.collectPost {
            buildingViewModel.asyncSubscribe().joinWithTimeout()
            buildingViewModel.updateBuildings().joinWithTimeout()
            buildingViewModel.buildings.getOrAwaitValue(emptyList())
            buildingViewModel.isListEmpty.getOrAwaitValue(true)
        }.apply {
            assertEquals(2, this.size)
            assertEquals(true, first())
            assertEquals(false, last())
        }
        buildingViewModel.unSubscribe()
    }
}