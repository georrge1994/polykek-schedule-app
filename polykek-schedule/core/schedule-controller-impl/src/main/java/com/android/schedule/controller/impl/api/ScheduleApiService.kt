package com.android.schedule.controller.impl.api

import com.android.core.retrofit.api.IApiService
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val SCHEDULE = "scheduler/{groupId}"

// https://ruz.spbstu.ru/api/v1/ruz/teachers/2559/scheduler?date=2020-06-08
private const val PROFESSORS_SCHEDULE = "teachers/{professorId}/scheduler"

/**
 * Schedule API-service.
 */
internal interface ScheduleApiService : IApiService {
    /**
     * Get week schedule.
     *
     * @param groupId Group id
     * @param date Date
     * @return [StudentWeekResponse]
     */
    @GET(SCHEDULE)
    suspend fun getWeekScheduleForGroup(
        @Path("groupId") groupId: Int,
        @Query("date") date: String
    ): Response<StudentWeekResponse>

    /**
     * Get schedule for professor.
     *
     * @param groupId Group id
     * @param date Date
     * @return [ProfessorWeekResponse]
     */
    @GET(PROFESSORS_SCHEDULE)
    suspend fun getScheduleForProfessor(
        @Path("professorId") groupId: Int,
        @Query("date") date: String
    ): Response<ProfessorWeekResponse>
}