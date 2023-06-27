package com.android.professors.search.viewModels

import android.app.Application
import com.android.common.models.professors.Professor
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.core.ui.models.ScheduleMode
import com.android.core.ui.mvi.MviViewModel
import com.android.feature.professors.R
import com.android.professors.search.mvi.ProfessorsSearchAction
import com.android.professors.search.mvi.ProfessorsSearchIntent
import com.android.professors.search.mvi.ProfessorsSearchState
import com.android.professors.search.useCases.ProfessorsSearchUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Named

private const val MIN_COUNT_OF_SYMBOLS = 3

/**
 * Provides logic for searching professors by name + professor list.
 *
 * @property application Application object to get context
 * @property backgroundMessageBus Common background message bus with snack-bar output
 * @property professorsSearchUseCase Professors search use case
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [ProfessorSearchViewModel]
 */
internal class ProfessorSearchViewModel @Inject constructor(
    private val application: Application,
    @Named(BACKGROUND_MESSAGE_BUS) private val backgroundMessageBus: MutableSharedFlow<String>,
    private val professorsSearchUseCase: ProfessorsSearchUseCase,
    private val savedItemsRoomRepository: ISavedItemsRoomRepository
) : MviViewModel<ProfessorsSearchIntent, ProfessorsSearchState, ProfessorsSearchAction>(
    ProfessorsSearchState.Default
) {
    override suspend fun dispatchIntent(intent: ProfessorsSearchIntent) {
        when (intent) {
            is ProfessorsSearchIntent.InitContent -> initContent(intent.scheduleMode)
            is ProfessorsSearchIntent.SearchProfessorsByKeyword -> searchProfessorsByKeyword(intent.keyword)
            is ProfessorsSearchIntent.SaveAndShowNextScreen -> selectProfessor(intent.professor)
        }
    }

    /**
     * Init content.
     *
     * @param scheduleMode Schedule mode
     */
    private suspend fun initContent(scheduleMode: ScheduleMode) = withContext(Dispatchers.Default) {
        if (scheduleMode == ScheduleMode.NEW_ITEM) {
            // Clear after first launch (actual only if user chosen the professor role).
            currentState.copyState(scheduleMode = scheduleMode, professors = emptyList()).emitState()
        } else {
            currentState.copyState(scheduleMode = scheduleMode).emitState()
        }
    }

    /**
     * Search professors by keyword.
     *
     * @param keyword Keyword
     */
    private suspend fun searchProfessorsByKeyword(keyword: String) = withContext(Dispatchers.IO) {
        if (keyword.length < MIN_COUNT_OF_SYMBOLS) {
            backgroundMessageBus.emit(application.getString(R.string.professors_search_wrong_length_of_name))
        } else {
            currentState.copyState(isLoading = true).emitState()
            currentState.copyState(
                isLoading = false,
                professors = professorsSearchUseCase.getProfessors(keyword) ?: emptyList(),
                messageId = R.string.professors_search_fragment_no_professors
            ).emitState()
        }
    }

    /**
     * Select professor and show the next screen.
     *
     * @param professor Professor
     */
    private suspend fun selectProfessor(professor: Professor) = withContext(Dispatchers.IO) {
        if (currentState.scheduleMode == ScheduleMode.WELCOME || currentState.scheduleMode == ScheduleMode.NEW_ITEM) {
            savedItemsRoomRepository.saveItemAndSelectIt(professor.toSavedItem())
        }
        ProfessorsSearchAction.ShowNextScreen(currentState.scheduleMode, professor).emitAction()
    }
}