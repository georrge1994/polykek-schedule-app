package com.android.feature.notes.adapters.recycler

import com.android.core.room.api.notes.Note

/**
 * Item model for note recycler view.
 *
 * @property note Note
 * @property isSelected Is selected item
 * @constructor Create [NoteItem]
 */
internal data class NoteItem(
    val note: Note,
    var isSelected: Boolean = false
)