package com.android.feature.schools.viewModels

import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.mvi.MviViewModel
import com.android.feature.schools.mvi.SchoolAction
import com.android.feature.schools.mvi.SchoolIntent
import com.android.feature.schools.mvi.SchoolState
import com.android.feature.schools.useCases.SchoolUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * School view model provides general logic for school-screen.
 *
 * @property schoolUseCase This use case is an interactor before school repository
 * @constructor Create [SchoolViewModel]
 */
internal class SchoolViewModel @Inject constructor(
    private val schoolUseCase: SchoolUseCase
) : MviViewModel<SchoolIntent, SchoolState, SchoolAction>(SchoolState.Default) {
    override suspend fun dispatchIntent(intent: SchoolIntent) {
        when (intent) {
            is SchoolIntent.InitContent -> updateSchools(intent.scheduleMode)
            is SchoolIntent.ShowGroups -> SchoolAction.ShowGroups(intent.school, currentState.scheduleMode).emitAction()
        }
    }

    /**
     * Update schools.
     *
     * @param scheduleMode Schedule mode
     */
    private suspend fun updateSchools(scheduleMode: ScheduleMode) = withContext(Dispatchers.IO) {
        currentState.copyState(scheduleMode = scheduleMode, isLoading = true).emitState()
        currentState.copyState(schools = schoolUseCase.getSchools(), isLoading = false).emitState()
    }
}