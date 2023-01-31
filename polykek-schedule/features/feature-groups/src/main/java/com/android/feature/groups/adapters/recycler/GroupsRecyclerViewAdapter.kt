package com.android.feature.groups.adapters.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.core.ui.adapters.BaseRecyclerViewAdapter
import com.android.core.ui.view.custom.ItemHeaderListView
import com.android.feature.groups.R
import com.android.feature.groups.models.Group

private const val TITLE_TYPE = 0
private const val GROUP_TYPE = 1

/**
 * Groups recycler view adapter.
 *
 * @property context Context
 * @property listener Item action listener
 * @constructor Create [GroupsRecyclerViewAdapter]
 */
internal class GroupsRecyclerViewAdapter(
    private val context: Context,
    private val listener: IGroupActions
) : BaseRecyclerViewAdapter<RecyclerView.ViewHolder, Any>() {
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.layoutManager = getLayoutManager()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder.itemViewType) {
            TITLE_TYPE -> (holder as TitleViewHolder).bindTo(items[position] as String)
            else -> (holder as GroupViewHolder).bindTo(items[position] as Group)
        }

    override fun getItemViewType(position: Int) = if (items[position] is Group)
        GROUP_TYPE
    else
        TITLE_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TITLE_TYPE -> TitleViewHolder(ItemHeaderListView(context))
        else -> GroupViewHolder(GroupItemView(context), listener)
    }

    /**
     * Get layout manager.
     *
     * @return [GridAutoFitLayoutManager]
     */
    private fun getLayoutManager() = GridAutoFitLayoutManager(
        context,
        context.resources.getDimensionPixelOffset(R.dimen.group_item_width)
    ).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = if (items[position] is Group) {
                SPAN_COUNT
            } else {
                spanCount
            }
        }
    }
}
