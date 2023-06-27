package com.android.feature.notes.useCases

import com.android.core.room.api.notes.INotesRoomRepository
import com.android.feature.notes.adapters.recycler.NoteItem
import com.android.feature.notes.models.NotesTabType
import com.android.feature.notes.viewModels.OWN_NOTE_ALIAS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Notes use case provides map of notes.
 *
 * @property notesRoomRepository Notes room repository to work with notes in the memory
 * @constructor Create [NotesUseCase]
 */
internal class NotesUseCase @Inject constructor(private val notesRoomRepository: INotesRoomRepository) {
    /**
     * Get notes.
     *
     * @param selectedItemId Selected item id
     * @param keyWord Key word
     * @return Map of notes
     */
    internal suspend fun getNotes(selectedItemId: Int?, keyWord: String?) = withContext(Dispatchers.IO) {
        mapOf(
            NotesTabType.BY_LESSONS to getNotesBySelectedItem(selectedItemId.toString(), keyWord ?: ""),
            NotesTabType.OWN_NOTES to getOwnNotes(keyWord ?: "")
        )
    }

    /**
     * Get notes by group/professor id.
     *
     * @param selectedItemId Id of group or professor
     * @param keyWord Key word
     * @return List of group notes
     */
    private suspend fun getNotesBySelectedItem(
        selectedItemId: String,
        keyWord: String
    ) = notesRoomRepository.getNotesWhichContains(selectedItemId, keyWord)
        .asSequence()
        .filter { !it.id.contains(OWN_NOTE_ALIAS) }
        .map { NoteItem(note = it) }
        .toList()

    /**
     * Get own notes.
     *
     * @param keyWord Key word
     * @return List of own notes
     */
    private suspend fun getOwnNotes(keyWord: String) = notesRoomRepository.getNotesWhichContains(
        OWN_NOTE_ALIAS,
        keyWord
    ).map { NoteItem(note = it) }

    /**
     * Delete notes by ids.
     *
     * @param noteIds Note ids
     */
    internal suspend fun deleteNotesByIds(noteIds: HashSet<String>): Unit = withContext(Dispatchers.IO) {
        notesRoomRepository.deleteNotesByIds(noteIds)
    }
}