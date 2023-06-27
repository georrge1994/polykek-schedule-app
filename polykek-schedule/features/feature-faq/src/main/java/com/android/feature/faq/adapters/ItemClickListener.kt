package com.android.feature.faq.adapters

/**
 * Item click listener for recycler view adapters.
 */
interface ItemClickListener {
    /**
     * On click.
     *
     * @param position Item position
     * @param isOpen Is open state
     */
    fun onClick(position: Int, isOpen: Boolean)
}