package com.android.feature.buildings.api

import com.android.common.models.api.Resource
import com.android.core.retrofit.api.common.BaseApiRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Building api repository.
 *
 * @property buildingApiService Building api
 * @constructor Create [BuildingApiRepository]
 */
@Singleton
internal class BuildingApiRepository @Inject constructor(
    private val buildingApiService: BuildingApiService,
) : BaseApiRepository() {
    /**
     * Get buildings.
     *
     * @return [Resource]
     */
    internal suspend fun getBuildings(): Resource<BuildingsResponse> = safeApiCall {
        buildingApiService.getBuildings()
    }
}