package com.android.feature.notes.viewModels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.notes.Note
import com.android.core.ui.viewModels.BaseViewModel
import com.android.feature.notes.R
import java.util.*
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
) : BaseViewModel() {
    private var note: Note? = null

    val noteLiveData = MutableLiveData<Note>()

    /**
     * Init.
     *
     * @param noteId Note id
     * @param lessonTitle Lesson title
     * @param groupId Group id
     */
    internal fun init(noteId: String?, lessonTitle: String?, groupId: String?) = launchInBackground {
        val checkedNoteId = noteId ?: "${groupId}$OWN_NOTE_ALIAS${Date().time}"
        note = notesRoomRepository.getNoteById(checkedNoteId) ?: Note(
            id = checkedNoteId,
            name = lessonTitle ?: application.getString(R.string.notes_fragment_own_note),
            header = "",
            body = ""
        )
        noteLiveData.postValue(note!!)
    }

    /**
     * Save header.
     *
     * @param text Text
     */
    internal fun saveHeader(text: String) {
        note?.header = text
    }

    /**
     * Save body.
     *
     * @param text Text
     */
    internal fun saveBody(text: String) {
        note?.body = text
    }

    /**
     * Delete note.
     */
    internal fun deleteNote() = launchInBackground {
        note?.let { checkedNote ->
            notesRoomRepository.deleteNoteById(checkedNote.id)
        }
        note = null
    }

    /**
     * Update note.
     */
    internal fun updateNote() = launchInBackground {
        note?.let { checkedNote ->
            if (checkedNote.isBlank()) {
                notesRoomRepository.deleteNoteById(checkedNote.id)
            } else {
                notesRoomRepository.insert(checkedNote)
            }
        }
    }
}