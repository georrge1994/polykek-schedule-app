package com.android.feature.faq.adapters

/**
 * Item click listener for recycler view adapters.
 */
interface ItemClickListener {
    /**
     * On click.
     *
     * @param position Item position
     */
    fun onClick(position: Int)
}