package com.android.feature.groups.adapters.recycler

import androidx.recyclerview.widget.RecyclerView
import com.android.core.ui.view.custom.ItemHeaderListView

/**
 * Title view holder.
 *
 * @property itemHeaderListView [ItemHeaderListView]
 * @constructor Create [TitleViewHolder]
 */
internal class TitleViewHolder(private val itemHeaderListView: ItemHeaderListView) : RecyclerView.ViewHolder(itemHeaderListView) {
    /**
     * Bind to.
     *
     * @param title Title
     */
    internal fun bindTo(title: String) {
        itemHeaderListView.text = title
    }
}