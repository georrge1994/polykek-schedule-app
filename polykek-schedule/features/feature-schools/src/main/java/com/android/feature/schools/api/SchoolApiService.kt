package com.android.feature.schools.api

import com.android.core.retrofit.api.IApiService
import retrofit2.Response
import retrofit2.http.GET

// https://ruz.spbstu.ru/api/v1/ruz/faculties
private const val SCHOOL_LIST = "faculties"

/**
 * School API-service.
 */
internal interface SchoolApiService : IApiService {
    /**
     * Get school list.
     *
     * @return [Response]
     */
    @GET(SCHOOL_LIST)
    suspend fun getSchools(): Response<SchoolsResponse>
}