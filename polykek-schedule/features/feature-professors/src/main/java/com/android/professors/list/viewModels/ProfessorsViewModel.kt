package com.android.professors.list.viewModels

import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.professors.list.mvi.ProfessorAction
import com.android.professors.list.mvi.ProfessorIntent
import com.android.professors.list.mvi.ProfessorState
import com.android.professors.list.useCases.ProfessorsUseCase
import com.android.schedule.controller.api.IScheduleController
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Professors view model provides professors, which hold lessons on the current week.
 *
 * @property scheduleController Schedule controller allows to synchronize UI changes between all tabs
 * @property professorsUseCase Professors use case provides fetching professors from week
 * @constructor Create [ProfessorsViewModel]
 */
internal class ProfessorsViewModel @Inject constructor(
    private val scheduleController: IScheduleController,
    private val professorsUseCase: ProfessorsUseCase
) : BaseSubscriptionViewModel<ProfessorIntent, ProfessorState, ProfessorAction>(ProfessorState.Default) {
    override suspend fun subscribe() {
        super.subscribe()
        // Subscribe to week flow.
        scheduleController.weekFlow
            .onEach { week ->
                week?.let {
                    currentState.copyState(professors = professorsUseCase.getProfessors(week)).emitState()
                } ?: currentState.copyState(professors = emptyList()).emitState()
            }.cancelableLaunchInBackground()
    }

    override suspend fun dispatchIntent(intent: ProfessorIntent) {
        when (intent) {
            ProfessorIntent.OpenFAQScreen -> ProfessorAction.OpenFAQScreen.emitAction()
            is ProfessorIntent.OpenProfessorScheduleScreen ->
                ProfessorAction.OpenProfessorScheduleScreen(intent.professor).emitAction()
            ProfessorIntent.OpenProfessorSearchScreen -> ProfessorAction.OpenProfessorSearchScreen.emitAction()
        }
    }
}