package com.android.feature.notes.mvi

import com.android.core.ui.mvi.MviAction

/**
 * Note editor action.
 *
 * @constructor Create empty constructor for note editor action
 */
internal sealed class NoteEditorAction : MviAction {
    /**
     * Edit after deleting note.
     */
    internal object Exit : NoteEditorAction()
}
