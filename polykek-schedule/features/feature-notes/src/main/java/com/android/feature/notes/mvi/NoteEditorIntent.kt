package com.android.feature.notes.mvi

import com.android.core.ui.mvi.MviIntent

/**
 * Note editor intent.
 *
 * @constructor Create empty constructor for note editor intent
 */
internal sealed class NoteEditorIntent : MviIntent {
    /**
     * Init content.
     *
     * @property noteId Note id
     * @property lessonTitle Lesson title
     * @property groupId Group id
     * @constructor Create [InitContent]
     */
    internal data class InitContent(
        val noteId: String?,
        val lessonTitle: String?,
        val groupId: String?
    ) : NoteEditorIntent()

    /**
     * Save header.
     *
     * @property text Text
     * @constructor Create [SaveHeader]
     */
    internal data class SaveHeader(val text: String) : NoteEditorIntent()

    /**
     * Save body.
     *
     * @property text Text
     * @constructor Create [SaveBody]
     */
    internal data class SaveBody(val text: String) : NoteEditorIntent()

    /**
     * Delete note.
     */
    internal object DeleteNote : NoteEditorIntent()

    /**
     * Update note.
     */
    internal object UpdateNote : NoteEditorIntent()
}