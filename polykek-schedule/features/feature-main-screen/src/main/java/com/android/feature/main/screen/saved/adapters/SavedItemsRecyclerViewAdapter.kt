package com.android.feature.main.screen.saved.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.common.models.savedItems.SavedItem
import com.android.feature.main.screen.saved.adapters.control.ControlItem
import com.android.feature.main.screen.saved.adapters.control.ControlItemView
import com.android.feature.main.screen.saved.adapters.control.ControlItemViewHolder
import com.android.feature.main.screen.saved.adapters.savedItem.SavedItemView
import com.android.feature.main.screen.saved.adapters.savedItem.SavedItemViewHolder

private const val SAVED_ITEM_TYPE = 0
private const val CONTROL_ITEM_TYPE = 1

/**
 * Saved items recycler view adapter.
 *
 * @property context Context
 * @property savedItemActions [ISaveItemMenuActions]
 * @constructor Create [SavedItemsRecyclerViewAdapter]
 */
internal class SavedItemsRecyclerViewAdapter(
    private val context: Context,
    private val savedItemActions: ISaveItemMenuActions
) : SwipeRecyclerViewAdapter<RecyclerView.ViewHolder, Any>(context) {
    override fun isActionAllowed(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Boolean {
        val item = items[viewHolder.bindingAdapterPosition]
        return item is SavedItem && !item.isSelected
    }

    override fun swipeAction(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        savedItemActions.onRemove(items[viewHolder.bindingAdapterPosition] as SavedItem)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when {
        holder.itemViewType == SAVED_ITEM_TYPE -> (holder as SavedItemViewHolder).bindTo(items[position] as SavedItem)
        holder.itemViewType == CONTROL_ITEM_TYPE && position == items.size - 3 ->
            (holder as ControlItemViewHolder).bindTo(items[position] as ControlItem, savedItemActions::addProfessor)
        holder.itemViewType == CONTROL_ITEM_TYPE && position == items.size - 2 ->
            (holder as ControlItemViewHolder).bindTo(items[position] as ControlItem, savedItemActions::addGroup)
        holder.itemViewType == CONTROL_ITEM_TYPE && position == items.size - 1 ->
            (holder as ControlItemViewHolder).bindTo(items[position] as ControlItem, savedItemActions::sayAboutScheduleError)
        else -> (holder as SavedItemViewHolder).bindTo(items[position] as SavedItem)
    }

    override fun getItemViewType(position: Int) = if (items[position] is SavedItem)
        SAVED_ITEM_TYPE
    else
        CONTROL_ITEM_TYPE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        SAVED_ITEM_TYPE -> SavedItemViewHolder(SavedItemView(context), savedItemActions)
        CONTROL_ITEM_TYPE -> ControlItemViewHolder(ControlItemView(context))
        else -> SavedItemViewHolder(SavedItemView(context), savedItemActions)
    }
}
