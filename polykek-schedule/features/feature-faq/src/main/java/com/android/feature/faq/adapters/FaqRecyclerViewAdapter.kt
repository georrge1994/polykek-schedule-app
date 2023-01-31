package com.android.feature.faq.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.android.core.ui.adapters.BaseRecyclerViewAdapter
import com.android.feature.faq.models.FAQ

/**
 * Faq recycler view adapter.
 *
 * @property context Context
 * @constructor Create [FaqRecyclerViewAdapter]
 */
internal class FaqRecyclerViewAdapter(private val context: Context) : BaseRecyclerViewAdapter<FaqViewHolder, FAQ>() {
    private var selectedPosition = -1

    private val listener = object : ItemClickListener {
        override fun onClick(position: Int) {
            TransitionManager.beginDelayedTransition(recyclerView!!, TransitionSet().addTransition(ChangeBounds()))
            if (position != selectedPosition) {
                if (selectedPosition != -1) {
                    notifyItemChanged(selectedPosition)
                }
                notifyItemChanged(position)
                selectedPosition = position
            } else {
                selectedPosition = -1
                notifyItemChanged(position)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(viewHolder: FaqViewHolder, position: Int) =
        viewHolder.bindTo(items[position], position == selectedPosition)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FaqViewHolder =
        FaqViewHolder(FaqItemView(context), listener)

    override fun detachRecyclerView() {
        super.detachRecyclerView()
        selectedPosition = -1
    }
}