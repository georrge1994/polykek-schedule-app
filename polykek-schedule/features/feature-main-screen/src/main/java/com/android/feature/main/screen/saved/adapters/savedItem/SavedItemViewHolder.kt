package com.android.feature.main.screen.saved.adapters.savedItem

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.common.models.savedItems.SavedItem
import com.android.feature.main.screen.saved.adapters.ISaveItemMenuActions

/**
 * Saved item view holder.
 *
 * @property savedItemView [SavedItemView]
 * @property listener Item action listener
 * @constructor Create [SavedItemViewHolder]
 */
internal class SavedItemViewHolder(
    private val savedItemView: SavedItemView,
    private val listener: ISaveItemMenuActions
) : RecyclerView.ViewHolder(savedItemView) {
    /**
     * Bind to.
     *
     * @param savedItem Saved item
     */
    internal fun bindTo(savedItem: SavedItem) = with(savedItem) {
        savedItemView.title.text = name
        savedItemView.checked.isVisible = isSelected
        savedItemView.setOnClickListener { listener.onClick(this) }
    }
}