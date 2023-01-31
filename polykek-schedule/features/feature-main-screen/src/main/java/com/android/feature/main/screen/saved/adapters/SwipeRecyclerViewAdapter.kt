package com.android.feature.main.screen.saved.adapters

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.android.core.ui.adapters.BaseRecyclerViewAdapter

/**
 * Swipe recycler view adapter.
 *
 * @param T Type of viewHolder
 * @param R Type of Item model
 * @constructor Create [SwipeRecyclerViewAdapter]
 *
 * @param context Context
 */
internal abstract class SwipeRecyclerViewAdapter<T : RecyclerView.ViewHolder, R : Any>(context: Context) : BaseRecyclerViewAdapter<T, R>() {
    private val itemTouchHelper = ItemTouchHelper(object : SwipeToDeleteCallback(context) {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            /**
             * To disable "swipe" for specific item return 0 here.
             * For example:
             * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
             * if (viewHolder?.adapterPosition == 0) return 0
             */
            if (!isActionAllowed(recyclerView, viewHolder))
                return 0

            return super.getMovementFlags(recyclerView, viewHolder)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            swipeAction(viewHolder, direction)
        }
    })

    /**
     * Is action allowed.
     *
     * @param recyclerView Recycler view
     * @param viewHolder View holder
     * @return Condition result
     */
    protected abstract fun isActionAllowed(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Boolean

    /**
     * Swipe action.
     *
     * @param viewHolder View holder
     * @param direction Direction
     */
    protected abstract fun swipeAction(viewHolder: RecyclerView.ViewHolder, direction: Int)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}