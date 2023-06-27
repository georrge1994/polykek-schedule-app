package com.android.feature.notes.adapters.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.ViewGroup
import com.android.core.ui.adapters.BaseRecyclerViewAdapter

/**
 * Notes recycler view adapter.
 *
 * @property context Context
 * @property noteActions [INoteItemActions]
 * @constructor Create [NotesRecyclerViewAdapter]
 */
internal class NotesRecyclerViewAdapter(
    private val context: Context,
    private val noteActions: INoteItemActions
) : BaseRecyclerViewAdapter<NoteViewHolder, NoteItem>() {
    private var isSelectionModeActivated: Boolean = false

    private val innerNoteActions = object : INoteItemActions {
        override fun onClick(position: Int, noteId: String) {
            noteActions.onClick(position, noteId)
            if (isSelectionModeActivated) {
                items[position].isSelected = !items[position].isSelected
                TransitionManager.beginDelayedTransition(recyclerView, AutoTransition())
                notifyItemChanged(position)
            }
        }

        override fun longPress(position: Int, noteId: String) {
            noteActions.longPress(position, noteId)
            items[position].isSelected = !items[position].isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoteViewHolder(NoteItemView(context), innerNoteActions)


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =
        holder.bindTo(items[position], isSelectionModeActivated)

    /**
     * Update selection mode - all items.
     *
     * @param isSelectionModeActivated Is selection mode activated
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateSelectionMode(isSelectionModeActivated: Boolean) {
        if (isSelectionModeActivated == this.isSelectionModeActivated)
            return

        this.isSelectionModeActivated = isSelectionModeActivated
        if (!isSelectionModeActivated)
            items.forEach { it.isSelected = false }
        TransitionManager.beginDelayedTransition(recyclerView, AutoTransition())
        notifyDataSetChanged()
    }
}