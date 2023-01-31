package com.android.feature.buildings.useCases

import com.android.common.models.api.Resource
import com.android.common.models.map.Building
import com.android.feature.buildings.api.BuildingApiRepository
import com.android.feature.buildings.api.BuildingsResponse
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.unitTest.base.BaseUnitTestForSubscriptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Building use case test for [BuildingUseCase].
 *
 * @constructor Create empty constructor for building use case test
 */
class BuildingUseCaseTest : BaseUnitTestForSubscriptions() {
    private val buildingConvertUseCase: BuildingConvertUseCase = mockk {
        coEvery { convertResponseToBuildings(any()) } returns emptyList()
        coEvery { getFilteredBuildings(any(), any()) } returns emptyList()
    }
    private val buildingApiRepository: BuildingApiRepository = mockk()
    private val backgroundMessageBus = MutableSharedFlow<String>(replay = 1)

    private val buildingUseCase = BuildingUseCase(
        buildingConvertUseCase = buildingConvertUseCase,
        buildingApiRepository = buildingApiRepository,
        backgroundMessageBus = backgroundMessageBus
    )

    /**
     * Get buildings error request.
     */
    @Test
    fun getBuildings_errorRequest() = runBlockingUnit {
        val resourceError = Resource.Error<BuildingsResponse>(errorMessage = "ooops")
        coEvery { buildingApiRepository.getBuildings() } returns resourceError
        assertEquals(emptyList<Building>(), buildingUseCase.getBuildings(null))
        backgroundMessageBus.subscribeAndCompareFirstValue("ooops").joinWithTimeout()
        coVerify(exactly = 1) { buildingConvertUseCase.getFilteredBuildings(any(), any()) }
    }

    /**
     * Get buildings success request.
     */
    @Test
    fun getBuildings_successRequest() = runBlocking {
        val successResource = Resource.Success(data = BuildingsResponse())
        coEvery { buildingApiRepository.getBuildings() } returns successResource
        assertEquals(emptyList<Building>(), buildingUseCase.getBuildings(null))
        coVerify(exactly = 1) {
            buildingConvertUseCase.convertResponseToBuildings(any())
            buildingConvertUseCase.getFilteredBuildings(any(), any())
        }
    }
}