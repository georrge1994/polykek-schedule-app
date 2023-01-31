package com.android.schedule.controller.impl.useCases

import com.android.common.models.schedule.Week
import com.android.core.retrofit.api.common.CatchResourceUseCase
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.schedule.controller.api.IProfessorScheduleUseCase
import com.android.schedule.controller.impl.api.ScheduleApiRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Professor schedule use case.
 *
 * @property scheduleResponseUseCase Converts response-week format to UI-week
 * @property scheduleApiRepository Api repository for student and professor
 * @constructor Create [ProfessorScheduleUseCase]
 *
 * @param backgroundMessageBus Common background message bus with snack-bar output
 */
internal class ProfessorScheduleUseCase @Inject constructor(
    private val scheduleResponseUseCase: ScheduleResponseConvertUseCase,
    private val scheduleApiRepository: ScheduleApiRepository,
    @Named(BACKGROUND_MESSAGE_BUS) backgroundMessageBus: MutableSharedFlow<String>
) : CatchResourceUseCase(backgroundMessageBus), IProfessorScheduleUseCase {
    /**
     * Get professor schedule.
     *
     * @param professorId Professor id
     * @param period Period
     * @return [Week] or null
     */
    override suspend fun getProfessorSchedule(professorId: Int, period: String): Week? =
        scheduleApiRepository.getProfessorSchedule(professorId, period).catchRequestError {
            scheduleResponseUseCase.convertToWeekFormat(it)
        }
}