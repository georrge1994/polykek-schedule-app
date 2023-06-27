package com.android.feature.buildings.api

import com.android.common.models.api.Resource
import com.android.test.support.unitTest.base.BaseApiRepositoryTest
import com.android.test.support.unitTest.checkNegativeResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Building api repository test for [BuildingApiRepository].
 *
 * @constructor Create empty constructor for building api repository test
 */
class BuildingApiRepositoryTest : BaseApiRepositoryTest() {
    private val buildingApiRepository = BuildingApiRepository(buildingApiService = getApi(BuildingApiService::class.java))

    /**
     * Get buildings 404.
     */
    @Test
    fun getBuildings_404() = runTest {
        enqueueResponse(code = 404)
        buildingApiRepository.getBuildings().checkNegativeResult()
    }

    /**
     * Get buildings 200.
     */
    @Test
    fun getBuildings_200() = runTest {
        enqueueResponse(fileName = "buildings-200.json", code = 200)
        buildingApiRepository.getBuildings().apply {
            assert(this is Resource.Success)
            assert(data?.buildingResponses?.isNotEmpty()== true)
            this.data?.buildingResponses?.get(9)?.apply {
                assertEquals(id, 21)
                assertEquals(name, "6-й учебный корпус")
                assertEquals(abbr, "6 к.")
                assertEquals(address, "")
            }
        }
    }
}