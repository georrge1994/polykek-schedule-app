package com.android.feature.notes.mvi

import com.android.core.ui.mvi.MviAction
import com.android.feature.notes.models.NotesTabType

/**
 * Notes action.
 *
 * @constructor Create empty constructor for notes action
 */
internal sealed class NotesAction : MviAction {
    /**
     * Open note editor.
     *
     * @property noteId Note id
     * @property tabType Tab type
     * @constructor Create [OpenNoteEditor]
     */
    internal data class OpenNoteEditor(val noteId: String, val tabType: NotesTabType) : NotesAction()

    /**
     * Open note editor new.
     *
     * @property selectedItemId Id of group or professor
     * @constructor Create [OpenNoteEditorNew]
     */
    internal data class OpenNoteEditorNew(val selectedItemId: Int?) : NotesAction()

    /**
     * Update toolbar.
     */
    internal data object UpdateToolbar : NotesAction()
}
