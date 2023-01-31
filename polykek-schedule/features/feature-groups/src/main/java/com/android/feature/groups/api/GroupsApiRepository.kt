package com.android.feature.groups.api

import com.android.common.models.api.Resource
import com.android.core.retrofit.api.common.BaseApiRepository
import javax.inject.Inject

/**
 * Provides api for fetching groups.
 *
 * @property groupApiService [GroupApiService]
 * @constructor Create [GroupsApiRepository]
 */
internal class GroupsApiRepository @Inject constructor(private val groupApiService: GroupApiService) : BaseApiRepository() {
    /**
     * Get groups.
     *
     * @param schoolId School id
     * @return [GroupsResponse] or null
     */
    internal suspend fun getGroups(schoolId: String): Resource<GroupsResponse> = safeApiCall {
        groupApiService.getGroups(schoolId)
    }
}