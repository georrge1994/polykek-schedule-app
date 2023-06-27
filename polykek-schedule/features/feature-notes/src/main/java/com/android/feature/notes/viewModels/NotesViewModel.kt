package com.android.feature.notes.viewModels

import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.viewModels.SearchViewModel
import com.android.feature.notes.models.NotesTabType
import com.android.feature.notes.mvi.NotesAction
import com.android.feature.notes.mvi.NotesIntent
import com.android.feature.notes.mvi.NotesState
import com.android.feature.notes.useCases.NotesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Notes view model provides logic for note screens.
 *
 * @property notesUseCase Notes room use case provides map of notes
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [NotesViewModel]
 */
internal class NotesViewModel @Inject constructor(
    private val notesUseCase: NotesUseCase,
    private val savedItemsRoomRepository: ISavedItemsRoomRepository
) : SearchViewModel<NotesIntent, NotesState, NotesAction>(NotesState.DefaultState) {
    private val removingNoteIds = HashSet<String>()
    private var selectedItemId: Int? = null

    override suspend fun subscribe() {
        super.subscribe()
        subscribeToSelectedItemFlow()
        updateNotes(keyWordFromLastState)
    }

    override suspend fun dispatchIntent(intent: NotesIntent) {
        when (intent) {
            is NotesIntent.KeyWordChanged -> updateNotes(intent.keyWord)
            is NotesIntent.LongPressByNote -> longPressByNote(intent.noteId)
            is NotesIntent.ClickByNote -> clickByNote(intent.noteId, intent.tabType)
            is NotesIntent.OpenNoteEditorNew -> NotesAction.OpenNoteEditorNew(selectedItemId).emitAction()
            is NotesIntent.DeleteSelectedNotes -> deleteSelectedNotes()
            is NotesIntent.ChangeTab -> changeTabPosition(intent.position)
        }
    }

    /**
     * Subscribe to selected item flow. It's needs to update lesson's notes, when user changes the selected group/professor.
     */
    private fun subscribeToSelectedItemFlow() =
        savedItemsRoomRepository.getSelectedItemFlow()
            .filterNotNull()
            .onEach {
                selectedItemId = it.id
                updateNotes(keyWordFromLastState)
            }.cancelableLaunchInBackground()

    /**
     * Update notes.
     *
     * @param keyWord Key word to search notes
     */
    private suspend fun updateNotes(keyWord: String?) = withContext(Dispatchers.IO) {
        currentState.copyState(
            keyWord = keyWord,
            notes = notesUseCase.getNotes(selectedItemId, keyWord)
        ).emitState()
    }

    /**
     * Long press by note.
     *
     * @param noteId Note id
     */
    private suspend fun longPressByNote(noteId: String) {
        val isNotSelectedYet = currentState.isSelectionModeEnabled.not()
        if (removingNoteIds.contains(noteId)) {
            removingNoteIds.remove(noteId)
        } else {
            removingNoteIds.add(noteId)
        }
        currentState.copyState(isSelectionModeEnabled = removingNoteIds.isNotEmpty()).emitState()
        // TODO: possible for jetpack compose I will simplify it.
        if (isNotSelectedYet) {
            NotesAction.UpdateToolbar.emitAction()
        }
    }

    /**
     * Click by note.
     *
     * @param noteId Note id
     * @param tabType Tab type
     */
    private suspend fun clickByNote(noteId: String, tabType: NotesTabType) {
        if (currentState.isSelectionModeEnabled) {
            if (removingNoteIds.contains(noteId)) {
                removingNoteIds.remove(noteId)
            } else {
                removingNoteIds.add(noteId)
            }
            currentState.copyState(isSelectionModeEnabled = removingNoteIds.isNotEmpty()).emitState()
        } else {
            NotesAction.OpenNoteEditor(noteId, tabType).emitAction()
        }
    }

    /**
     * Delete selected notes.
     */
    private suspend fun deleteSelectedNotes() = withContext(Dispatchers.IO) {
        currentState.copyState(isSelectionModeEnabled = false).emitState()
        notesUseCase.deleteNotesByIds(removingNoteIds)
        removingNoteIds.clear()
        updateNotes(keyWordFromLastState)
        NotesAction.UpdateToolbar.emitAction()
    }

    /**
     * Change tab position.
     *
     * @param tabPosition Tab position
     */
    private suspend fun changeTabPosition(tabPosition: Int) = withContext(Dispatchers.Default) {
        currentState.copyState(tabPosition = tabPosition).emitState()
    }
}