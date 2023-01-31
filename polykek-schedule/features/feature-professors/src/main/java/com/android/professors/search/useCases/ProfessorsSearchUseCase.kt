package com.android.professors.search.useCases

import com.android.common.models.professors.Professor
import com.android.core.retrofit.api.common.CatchResourceUseCase
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.professors.search.api.ProfessorsApiRepository
import com.android.professors.search.api.TeachersResponse
import com.android.schedule.controller.api.ITeacherToProfessorConvertor
import com.android.schedule.controller.api.week.lesson.TeacherResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Professors search use case.
 *
 * @property professorScheduleRepository Provides api to search professors
 * @property teacherToProfessorConvertor Convert [TeacherResponse] to [Professor]
 * @constructor Create [ProfessorsSearchUseCase]
 *
 * @param backgroundMessageBus Common background message bus with snack-bar output
 */
@Singleton
internal class ProfessorsSearchUseCase @Inject constructor(
    private val professorScheduleRepository: ProfessorsApiRepository,
    private val teacherToProfessorConvertor: ITeacherToProfessorConvertor,
    @Named(BACKGROUND_MESSAGE_BUS) backgroundMessageBus: MutableSharedFlow<String>
) : CatchResourceUseCase(backgroundMessageBus) {
    /**
     * Get professors.
     *
     * @param keyWord Key word
     * @return Professors
     */
    internal suspend fun getProfessors(keyWord: String): List<Professor>? =
        professorScheduleRepository.getTeachersByKey(keyWord).catchRequestError { teachersResponse ->
            teachersResponse.convertToProfessors()
        }

    /**
     * Convert to professors.
     *
     * @receiver [TeachersResponse] or null
     * @return [Professor] list
     */
    private fun TeachersResponse?.convertToProfessors(): List<Professor> = ArrayList<Professor>().apply {
        this@convertToProfessors?.teachers?.forEach { teacher ->
            teacherToProfessorConvertor.convertTeacherToProfessor(teacher)?.let { professor ->
                add(professor)
            }
        }
    }.sortedBy { it.fullName }
}