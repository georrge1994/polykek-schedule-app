package com.android.feature.notes.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.viewModels.SearchViewModel
import com.android.feature.notes.adapters.recycler.NoteItem
import com.android.feature.notes.models.NotesTabTypes
import com.android.shared.code.utils.liveData.EventLiveData
import com.android.shared.code.utils.syntaxSugar.postValueIfChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Notes view model provides logic for note screens.
 *
 * @property notesRoomRepository Notes room repository to work with notes in the memory
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [NotesViewModel]
 */
internal class NotesViewModel @Inject constructor(
    private val notesRoomRepository: INotesRoomRepository,
    private val savedItemsRoomRepository: ISavedItemsRoomRepository
) : SearchViewModel() {
    private val removingNoteIds = HashSet<String>()
    private val notesByLessons = MutableLiveData<List<NoteItem>>()
    private val ownNotes = MutableLiveData<List<NoteItem>>()
    val selectionModeState = EventLiveData<Boolean>()
    var selectedItemId: Int? = null
        private set

    override suspend fun subscribe() {
        super.subscribe()
        subscribeToSelectedItemFlow()
        updateNotes()
    }

    override suspend fun keyWordWasChanged(keyWord: String?) {
        super.keyWordWasChanged(keyWord)
        updateNotes()
    }

    /**
     * Subscribe to selected item flow. It's needs to update lesson's notes, when user changes the selected group/professor.
     */
    private fun subscribeToSelectedItemFlow() =
        savedItemsRoomRepository.getSelectedItemFlow()
            .filterNotNull()
            .onEach {
                selectedItemId = it.id
                updateNotes()
            }.cancelableLaunchInBackground()

    /**
     * Update notes.
     */
    private suspend fun updateNotes() {
        notesRoomRepository.getNotesWhichContains(selectedItemId.toString(), keyWord)
            .filter { !it.id.contains(OWN_NOTE_ALIAS) }
            .map { NoteItem(note = it) }
            .let { notesByLessons.postValueIfChanged(it) }
        notesRoomRepository.getNotesWhichContains(OWN_NOTE_ALIAS, keyWord)
            .map { NoteItem(note = it) }
            .let { ownNotes.postValueIfChanged(it) }
    }

    /**
     * Get notes live data.
     *
     * @param notesTabTypes Notes tab types
     * @return Specific live data for list fragment
     */
    internal fun getNotesLiveData(notesTabTypes: NotesTabTypes) = when (notesTabTypes) {
        NotesTabTypes.BY_LESSONS -> notesByLessons
        NotesTabTypes.OWN_NOTES -> ownNotes
    }

    /**
     * Get is empty notes livedata.
     *
     * @param notesTabTypes Notes tab types
     * @return Specific live data for list fragment
     */
    internal fun getIsEmptyNotesLivedata(notesTabTypes: NotesTabTypes) = when (notesTabTypes) {
        NotesTabTypes.BY_LESSONS -> notesByLessons
        NotesTabTypes.OWN_NOTES -> ownNotes
    }.let { liveData ->
        Transformations.map(liveData) { notes ->
            notes.isNullOrEmpty()
        }
    }

    /**
     * click by item (select/deselect).
     *
     * @param noteId Note id
     */
    internal fun clickByItem(noteId: String) = launchInBackground {
        if (removingNoteIds.contains(noteId)) {
            removingNoteIds.remove(noteId)
            if (removingNoteIds.isEmpty()) {
                selectionModeState.postValue(false)
            }
        } else {
            selectionModeState.postValueIfChanged(true)
            removingNoteIds.add(noteId)
        }
    }

    /**
     * Delete selected notes.
     */
    internal fun deleteSelectedNotes() = launchInBackground {
        notesRoomRepository.deleteNotesByIds(removingNoteIds)
        selectionModeState.postValue(false)
        removingNoteIds.clear()
        updateNotes()
    }
}