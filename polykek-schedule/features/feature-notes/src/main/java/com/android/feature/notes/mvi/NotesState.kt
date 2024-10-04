package com.android.feature.notes.mvi

import com.android.core.ui.mvi.SearchState
import com.android.feature.notes.adapters.recycler.NoteItem
import com.android.feature.notes.models.NotesTabType

/**
 * Notes state properties.
 */
internal interface NotesStateProperties {
    val isSelectionModeEnabled: Boolean
    val tabPosition: Int
    val notes: Map<NotesTabType, List<NoteItem>>
}

/**
 * Notes state.
 *
 * @constructor Create empty constructor for notes state
 */
internal sealed class NotesState : SearchState, NotesStateProperties {
    /**
     * Default state.
     */
    internal data object DefaultState : NotesState() {
        override val keyWord: String? = null
        override val isSelectionModeEnabled: Boolean = false
        override val tabPosition: Int = 0
        override val notes: Map<NotesTabType, List<NoteItem>> = emptyMap()
    }

    /**
     * Update.
     *
     * @property keyWord Search key word
     * @property tabPosition Tab position
     * @property isSelectionModeEnabled Selection mode enabled
     * @property notes Notes
     * @constructor Create [Update]
     */
    internal data class Update(
        override val keyWord: String?,
        override val tabPosition: Int,
        override val isSelectionModeEnabled: Boolean,
        override val notes: Map<NotesTabType, List<NoteItem>>,
    ) : NotesState()

    /**
     * Copy state.
     *
     * @param keyWord Key word
     * @param tabPosition Tab position
     * @param isSelectionModeEnabled Is selection mode enabled
     * @param notes Notes
     * @return [NotesState]
     */
    internal fun copyState(
        keyWord: String? = this.keyWord,
        tabPosition: Int = this.tabPosition,
        isSelectionModeEnabled: Boolean = this.isSelectionModeEnabled,
        notes: Map<NotesTabType, List<NoteItem>> = this.notes
    ): NotesState = Update(keyWord, tabPosition, isSelectionModeEnabled, notes)
}