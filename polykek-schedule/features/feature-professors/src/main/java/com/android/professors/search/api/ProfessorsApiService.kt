package com.android.professors.search.api

import com.android.core.retrofit.api.IApiService
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// https://ruz.spbstu.ru/api/v1/ruz/search/teachers?q=%D0%9F%D0%B5
private const val TEACHERS_BY_KEY = "search/teachers"

/**
 * Professors api service.
 */
internal interface ProfessorsApiService : IApiService {
    /**
     * Get teachers by key.
     *
     * @param keyWord Key word
     * @return [TeachersResponse]
     */
    @GET(TEACHERS_BY_KEY)
    suspend fun getTeachersByKey(
        @Query("q") keyWord: String
    ): Response<TeachersResponse>
}