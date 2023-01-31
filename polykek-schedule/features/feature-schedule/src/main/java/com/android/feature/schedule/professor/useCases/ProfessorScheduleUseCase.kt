package com.android.feature.schedule.professor.useCases

import com.android.common.models.schedule.Week
import com.android.schedule.controller.api.IProfessorScheduleUseCase
import com.android.shared.code.utils.markers.IUseCase
import java.util.*
import javax.inject.Inject

/**
 * Professor schedule use case.
 *
 * @property professorScheduleUseCase Provides request to fetch professor schedule
 * @constructor Create [ProfessorScheduleUseCase]
 */
internal class ProfessorScheduleUseCase @Inject constructor(
    private val professorScheduleUseCase: IProfessorScheduleUseCase
) : IUseCase {
    /**
     * Get professor schedule.
     *
     * @param professorId Professor id
     * @param period Period
     * @return [Week] or null
     */
    internal suspend fun getProfessorSchedule(professorId: Int, period: String): Week? =
        professorScheduleUseCase.getProfessorSchedule(professorId, period)

    /**
     * Get recycler items.
     *
     * @param week Week
     * @return Lessons with day headers
     */
    internal fun getRecyclerItems(week: Week?): List<Any> {
        week ?: return Collections.emptyList()
        val items = ArrayList<Any>()
        week.days.values.forEach { day ->
            items.add(day.date)
            day.lessons.forEach {
                items.add(it)
            }
        }
        return items
    }
}