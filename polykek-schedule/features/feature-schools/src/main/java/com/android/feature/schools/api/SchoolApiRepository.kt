package com.android.feature.schools.api

import com.android.common.models.api.Resource
import com.android.core.retrofit.api.common.BaseApiRepository
import javax.inject.Inject

/**
 * Provides api-request to get school list.
 *
 * @property schoolApiService [SchoolApiService]
 * @constructor Create [SchoolApiRepository]
 */
internal class SchoolApiRepository @Inject constructor(private val schoolApiService: SchoolApiService) : BaseApiRepository() {
    /**
     * Get school list.
     *
     * @return [Resource] with school list or an error
     */
    internal suspend fun getSchools(): Resource<SchoolsResponse> = safeApiCall {
        schoolApiService.getSchools()
    }
}