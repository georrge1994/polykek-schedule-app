package com.android.feature.notes.viewModels

import android.app.Application
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.notes.Note
import com.android.core.ui.mvi.MviViewModel
import com.android.feature.notes.R
import com.android.feature.notes.mvi.NoteEditorAction
import com.android.feature.notes.mvi.NoteEditorIntent
import com.android.feature.notes.mvi.NoteEditorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

internal const val OWN_NOTE_ALIAS = "_own_note_"

/**
 * Note editor view model.
 *
 * @property application Application object to get context
 * @property notesRoomRepository Notes room repository
 * @constructor Create [NoteEditorViewModel]
 */
internal class NoteEditorViewModel @Inject constructor(
    private val application: Application,
    private val notesRoomRepository: INotesRoomRepository
) : MviViewModel<NoteEditorIntent, NoteEditorState, NoteEditorAction>(NoteEditorState.Default) {
    private val mutex = Mutex()
    private var note: Note? = null

    override suspend fun dispatchIntent(intent: NoteEditorIntent) {
        when (intent) {
            is NoteEditorIntent.InitContent -> init(intent.noteId, intent.lessonTitle, intent.groupId)
            is NoteEditorIntent.SaveBody -> saveBody(intent.text)
            is NoteEditorIntent.SaveHeader -> saveHeader(intent.text)
            NoteEditorIntent.UpdateNote -> saveNote()
            NoteEditorIntent.DeleteNote -> deleteNote()
        }
    }

    /**
     * Init.
     *
     * @param noteId Note id
     * @param lessonTitle Lesson title
     * @param groupId Group id
     */
    private suspend fun init(noteId: String?, lessonTitle: String?, groupId: String?) = withContext(Dispatchers.IO) {
        val checkedNoteId = noteId ?: "${groupId}$OWN_NOTE_ALIAS${Date().time}"
        note = notesRoomRepository.getNoteById(checkedNoteId) ?: Note(
            id = checkedNoteId,
            name = lessonTitle ?: application.getString(R.string.notes_fragment_own_note),
            header = "",
            body = ""
        )
        state.value.copyState(note).emitState()
    }

    /**
     * Save header.
     *
     * @param text Text
     */
    private suspend fun saveHeader(text: String) = mutex.withLock {
        note?.header = text
    }

    /**
     * Save body.
     *
     * @param text Text
     */
    private suspend fun saveBody(text: String) = mutex.withLock {
        note?.body = text
    }

    /**
     * Delete note.
     */
    private suspend fun deleteNote() = withContext(Dispatchers.IO) {
        note?.let { checkedNote ->
            notesRoomRepository.deleteNoteById(checkedNote.id)
        }
        mutex.withLock {
            note = null
        }
        NoteEditorAction.Exit.emitAction()
    }

    /**
     * Save note.
     */
    private suspend fun saveNote() = withContext(Dispatchers.IO) {
        note?.let { checkedNote ->
            if (checkedNote.isBlank()) {
                notesRoomRepository.deleteNoteById(checkedNote.id)
            } else {
                notesRoomRepository.insert(checkedNote)
            }
        }
    }
}