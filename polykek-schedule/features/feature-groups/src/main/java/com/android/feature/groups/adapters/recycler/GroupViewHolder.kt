package com.android.feature.groups.adapters.recycler

import androidx.recyclerview.widget.RecyclerView
import com.android.feature.groups.models.Group

/**
 * Group view holder.
 *
 * @property itemListView [GroupItemView]
 * @property listener Item action listener
 * @constructor Create [GroupViewHolder]
 */
internal class GroupViewHolder(
    private val itemListView: GroupItemView,
    private val listener: IGroupActions
) : RecyclerView.ViewHolder(itemListView) {
    /**
     * Bind to.
     *
     * @param group Group
     */
    internal fun bindTo(group: Group) = with(group) {
        itemListView.text = nameMultiLines
        itemListView.setOnClickListener { listener.onClick(this) }
    }
}