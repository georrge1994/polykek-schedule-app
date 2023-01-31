package com.android.core.ui.adapters

import android.annotation.SuppressLint
import android.transition.Fade
import android.transition.TransitionManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * Base recycler view adapter.
 *
 * @param T Type of viewHolders
 * @param R Type of data model
 * @constructor Create empty constructor for base recycler view adapter
 */
abstract class BaseRecyclerViewAdapter<T : RecyclerView.ViewHolder, R : Any> : RecyclerView.Adapter<T>() {
    protected var recyclerView: RecyclerView? = null
    protected open val items = ArrayList<R>()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun getItemCount() = items.size

    override fun getItemId(position: Int) = items[position].hashCode().toLong()

    /**
     * Update items.
     *
     * @param items Items
     */
    fun updateItems(items: List<R>?) {
        items ?: return
        recyclerView?.let { TransitionManager.beginDelayedTransition(recyclerView, Fade()) }
        val diff = calcDiff(this.items, items)
        this.items.clear()
        this.items.addAll(items)
        diff.dispatchUpdatesTo(this)
    }

    /**
     * Detach recycler view.
     */
    @SuppressLint("NotifyDataSetChanged")
    open fun detachRecyclerView() {
        recyclerView = null
        this.items.clear()
        notifyDataSetChanged()
    }

    /**
     * Calc diff.
     *
     * @param old Old items
     * @param new New items
     * @return [DiffUtil.DiffResult]
     */
    private fun calcDiff(old: List<R>, new: List<R>) = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == new[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            old[oldItemPosition] == new[newItemPosition]

        override fun getOldListSize() = old.size

        override fun getNewListSize() = new.size
    })
}