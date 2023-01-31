package com.android.professors.search.api

import com.android.common.models.api.Resource
import com.android.core.retrofit.api.common.BaseApiRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Professors api repository.
 *
 * @property professorsApiService Provides search by teachers
 * @constructor Create [ProfessorsApiRepository]
 */
@Singleton
internal class ProfessorsApiRepository @Inject constructor(
    private val professorsApiService: ProfessorsApiService
) : BaseApiRepository() {
    /**
     * Get teachers by key.
     *
     * @param keyWord Key word
     * @return [Resource]
     */
    internal suspend fun getTeachersByKey(keyWord: String): Resource<TeachersResponse> = if (keyWord.isNotEmpty()) {
        safeApiCall { professorsApiService.getTeachersByKey(keyWord) }
    } else {
        Resource.Error("Empty keyword")
    }
}