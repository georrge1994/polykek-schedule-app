package com.android.professors.list.useCases

import com.android.common.models.professors.Professor
import com.android.common.models.schedule.Week
import com.android.shared.code.utils.markers.IUseCase
import javax.inject.Inject

/**
 * Professors use case provides fetching professors from week.
 *
 * @constructor Create empty constructor for professors use case
 */
internal class ProfessorsUseCase @Inject constructor() : IUseCase {
    /**
     * Get all professors from week schedule.
     *
     * @param week Week
     * @return List of [Professor]
     */
    internal fun getProfessors(week: Week): List<Professor> = HashSet<Professor>().apply {
        week.days.forEach { day ->
            day.value.lessons.forEach { lesson ->
                lesson.professors.forEach { professor ->
                    professor?.let {
                        add(it)
                    }
                }
            }
        }
    }.toList()
}