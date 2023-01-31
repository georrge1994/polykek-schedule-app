package com.android.feature.buildings.api

import com.android.core.retrofit.api.IApiService
import retrofit2.Response
import retrofit2.http.GET

// https://ruz.spbstu.ru/api/v1/ruz/buildings
private const val BUILDING_LIST = "buildings"

/**
 * Building api service.
 */
internal interface BuildingApiService : IApiService {
    /**
     * Get buildings.
     *
     * @return [Response]
     */
    @GET(BUILDING_LIST)
    suspend fun getBuildings(): Response<BuildingsResponse>
}