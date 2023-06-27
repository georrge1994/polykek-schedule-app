package com.android.feature.notes.mvi

import com.android.core.room.api.notes.Note
import com.android.core.ui.mvi.MviState

/**
 * Note editor state properties.
 */
internal interface NoteEditorStateProperties {
    val note: Note?
}

/**
 * Note editor state.
 *
 * @constructor Create empty constructor for note editor state
 */
internal sealed class NoteEditorState : MviState, NoteEditorStateProperties {
    /**
     * Default.
     */
    internal object Default : NoteEditorState() {
        override val note: Note? = null
    }

    /**
     * Update.
     *
     * @property note Note
     * @constructor Create [Update]
     */
    internal data class Update(override val note: Note?) : NoteEditorState()

    /**
     * Copy state.
     *
     * @param note Note
     * @return [NoteEditorState]
     */
    internal fun copyState(note: Note? = this.note): NoteEditorState = Update(note)
}