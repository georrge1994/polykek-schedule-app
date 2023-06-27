package com.android.feature.notes.mvi

import com.android.core.ui.mvi.SearchIntent
import com.android.feature.notes.models.NotesTabType

/**
 * Notes intent.
 *
 * @constructor Create empty constructor for notes intent
 */
internal sealed class NotesIntent : SearchIntent {
    /**
     * Key word changed.
     *
     * @property keyWord Key word
     * @constructor Create [KeyWordChanged]
     */
    internal data class KeyWordChanged(val keyWord: String?) : NotesIntent()

    /**
     * Change tab.
     *
     * @property position Position
     * @constructor Create [ChangeTab]
     */
    internal data class ChangeTab(val position: Int) : NotesIntent()

    /**
     * Delete selected notes.
     */
    internal object DeleteSelectedNotes : NotesIntent()

    /**
     * Open note editor new.
     */
    internal object OpenNoteEditorNew : NotesIntent()

    /**
     * Click by note.
     *
     * @property noteId Note id
     * @property tabType Tab type
     * @constructor Create [ClickByNote]
     */
    internal data class ClickByNote(val noteId: String, val tabType: NotesTabType) : NotesIntent()

    /**
     * Long press by note.
     *
     * @property noteId Note id
     * @constructor Create [LongPressByNote]
     */
    internal data class LongPressByNote(val noteId: String) : NotesIntent()
}