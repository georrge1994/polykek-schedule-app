package com.android.schedule.controller.api

import com.android.common.models.schedule.Week

/**
 * Provides professor schedule use case.
 */
interface IProfessorScheduleUseCase {
    /**
     * Get professor schedule.
     *
     * @param professorId Professor id
     * @param period Period
     * @return [Week] or null
     */
    suspend fun getProfessorSchedule(professorId: Int, period: String): Week?
}