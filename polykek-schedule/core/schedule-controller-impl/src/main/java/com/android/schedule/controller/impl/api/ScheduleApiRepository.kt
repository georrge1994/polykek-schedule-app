package com.android.schedule.controller.impl.api

import com.android.common.models.api.Resource
import com.android.core.retrofit.api.common.BaseApiRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Schedule api repository for student and professor.
 *
 * @property scheduleApiService Schedule API-service
 * @constructor Create [ScheduleApiRepository]
 */
@Singleton
internal class ScheduleApiRepository @Inject constructor(private val scheduleApiService: ScheduleApiService) : BaseApiRepository() {
    // Cached values for student request.
    private var cachedRequestResultStudentWeek: Response<StudentWeekResponse>? = null
    private var cachedGroupId: Int? = null
    private var cachedGroupPeriod: String? = null

    // Cached values for professor request.
    private var cachedRequestResultProfessorWeek: Response<ProfessorWeekResponse>? = null
    private var cachedProfessorId: Int? = null
    private var cachedProfessorPeriod: String? = null

    /**
     * Get schedule for group.
     *
     * @param groupId Group id
     * @param period Period
     * @return [Resource]
     */
    internal suspend fun getScheduleForGroup(groupId: Int, period: String): Resource<StudentWeekResponse> = safeApiCall {
        if (groupId == cachedGroupId && period == cachedGroupPeriod && cachedRequestResultStudentWeek?.isSuccessful == true)
            return@safeApiCall cachedRequestResultStudentWeek!!
        cachedRequestResultStudentWeek = scheduleApiService.getWeekScheduleForGroup(groupId, period)
        this.cachedGroupId = groupId
        this.cachedGroupPeriod = period
        cachedRequestResultStudentWeek!!
    }

    /**
     * Get professor schedule.
     *
     * @param professorId Professor id
     * @param period Period
     * @return [Resource]
     */
    internal suspend fun getProfessorSchedule(professorId: Int, period: String): Resource<ProfessorWeekResponse> = safeApiCall {
        if (professorId == cachedProfessorId && period == cachedProfessorPeriod && cachedRequestResultProfessorWeek?.isSuccessful == true)
            return@safeApiCall cachedRequestResultProfessorWeek!!
        cachedRequestResultProfessorWeek = scheduleApiService.getScheduleForProfessor(professorId, period)
        this.cachedProfessorId = professorId
        this.cachedProfessorPeriod = period
        cachedRequestResultProfessorWeek!!
    }
}