package com.android.feature.groups.api

import com.android.core.retrofit.api.IApiService
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// https://ruz.spbstu.ru/api/v1/ruz/faculties/100/groups
private const val GROUP_LIST = "faculties/{schoolId}/groups"

/**
 * Group service api.
 */
internal interface GroupApiService : IApiService {
    /**
     * Get groups for [schoolId].
     *
     * @param schoolId School id
     * @return [GroupsResponse]
     */
    @GET(GROUP_LIST)
    suspend fun getGroups(@Path("schoolId") schoolId: String): Response<GroupsResponse>
}