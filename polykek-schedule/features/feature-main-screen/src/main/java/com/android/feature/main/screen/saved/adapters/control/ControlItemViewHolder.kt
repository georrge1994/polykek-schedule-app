package com.android.feature.main.screen.saved.adapters.control

import androidx.recyclerview.widget.RecyclerView

/**
 * Control item view holder.
 *
 * @property controlItemView [ControlItemView]
 * @constructor Create [ControlItemViewHolder]
 */
internal class ControlItemViewHolder(private val controlItemView: ControlItemView) : RecyclerView.ViewHolder(controlItemView) {
    /**
     * Bind to.
     *
     * @param controlItem Control item
     * @param action Action
     */
    internal fun bindTo(controlItem: ControlItem, action: () -> Unit) = with(controlItemView) {
        text = context.getString(controlItem.textId)
        setCompoundDrawablesWithIntrinsicBounds(controlItem.iconId, 0, 0, 0)
        setOnClickListener { action.invoke() }
    }
}