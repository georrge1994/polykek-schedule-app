package com.android.feature.notes.adapters.recycler

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.feature.notes.R

/**
 * Note view holder.
 *
 * @property noteItemView [NoteItemView]
 * @property listener Item action listener
 * @constructor Create [NoteViewHolder]
 */
internal class NoteViewHolder(
    private val noteItemView: NoteItemView,
    private val listener: INoteItemActions
) : RecyclerView.ViewHolder(noteItemView) {
    /**
     * Bind to.
     *
     * @param noteItem Note item
     * @param isSelectionModeActivated Is selection mode activated
     */
    internal fun bindTo(noteItem: NoteItem, isSelectionModeActivated: Boolean) = with(noteItem.note) {
        noteItemView.parentLayout.background = ContextCompat.getDrawable(
            noteItemView.context,
            if (noteItem.isSelected)
                R.drawable.background_selected_item
            else
                R.drawable.white_rounded_background
        )

        if (name == noteItemView.context.getString(R.string.notes_fragment_own_note)) {
            noteItemView.name.updateTextAndVisibility(header)
            noteItemView.title.updateTextAndVisibility("")
        } else {
            noteItemView.name.updateTextAndVisibility(name)
            noteItemView.title.updateTextAndVisibility(header)
        }

        noteItemView.body.updateTextAndVisibility(body)
        noteItemView.checkBox.isVisible = isSelectionModeActivated
        noteItemView.checkBox.isChecked = noteItem.isSelected

        noteItemView.setOnClickListener { listener.onClick(bindingAdapterPosition, id) }
        noteItemView.setOnLongClickListener {
            listener.longPress(bindingAdapterPosition, id)
            true
        }
    }

    /**
     * Update text and visibility.
     *
     * @receiver [TextView]
     * @param text Text
     */
    private fun TextView.updateTextAndVisibility(text: String) {
        this.text = text
        this.isVisible = text.isNotEmpty()
    }
}