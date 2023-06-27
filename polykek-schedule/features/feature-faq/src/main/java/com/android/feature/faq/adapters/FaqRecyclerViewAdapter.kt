package com.android.feature.faq.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.android.core.ui.adapters.BaseRecyclerViewAdapter
import com.android.feature.faq.models.FAQ

/**
 * Faq recycler view adapter.
 *
 * @property context Context
 * @constructor Create [FaqRecyclerViewAdapter]
 */
internal class FaqRecyclerViewAdapter(
    private val context: Context,
    private val listener: ItemClickListener
) : BaseRecyclerViewAdapter<FaqViewHolder, FAQ>() {
    override fun onBindViewHolder(viewHolder: FaqViewHolder, position: Int) = viewHolder.bindTo(items[position])

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FaqViewHolder =
        FaqViewHolder(FaqItemView(context), listener)

    override fun updateItems(items: List<FAQ>?, transitionAnimation: Transition) {
        if (this.items.isEmpty()) {
            super.updateItems(items, transitionAnimation)
        } else {
            // Method is overridden to avoid animation glitch (seems diff utils has some problem with Transition).
            val newOpenedPosition = items?.indexOfFirst { it.isOpened } ?: -1
            val oldOpenedPosition = this.items.indexOfFirst { it.isOpened }
            this.items.clear()
            this.items.addAll(items ?: emptyList())
            TransitionManager.beginDelayedTransition(recyclerView!!, transitionAnimation)
            if (newOpenedPosition != oldOpenedPosition && oldOpenedPosition in 0 until itemCount)
                notifyItemChanged(oldOpenedPosition)
            if (newOpenedPosition in 0 until itemCount)
                notifyItemChanged(newOpenedPosition)
        }
    }
}