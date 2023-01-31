package com.android.feature.groups.adapters.recycler

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.feature.groups.R
import kotlin.math.max

internal const val SPAN_COUNT = 1

/**
 * Grid auto fit layout manager.
 *
 * @constructor Create [GridAutoFitLayoutManager]
 *
 * @param context Context object
 * @param columnWidth Recycler view item width
 */
internal class GridAutoFitLayoutManager(context: Context?, columnWidth: Int) : GridLayoutManager(context, SPAN_COUNT) {
    private var mColumnWidth: Int = 0
    private var mColumnWidthChanged = false

    init {
        // Initially set spanCount to 1, will be changed automatically later.
        setColumnWidth(checkedColumnWidth(context, columnWidth))
    }

    /**
     * Checked column width.
     *
     * @param context Context
     * @param columnWidth Column width
     * @return Checked width column
     */
    private fun checkedColumnWidth(context: Context?, columnWidth: Int): Int = if (columnWidth < 0) {
        context?.resources?.getDimensionPixelOffset(R.dimen.group_item_width) ?: 100
    } else {
        columnWidth
    }

    /**
     * Set column width.
     *
     * @param newColumnWidth New column width
     */
    private fun setColumnWidth(newColumnWidth: Int) {
        if (newColumnWidth > 0 && newColumnWidth != mColumnWidth) {
            mColumnWidth = newColumnWidth
            mColumnWidthChanged = true
        }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        val width = width
        val height = height
        if (mColumnWidthChanged && width > 0 && height > 0) {
            val totalSpace = if (orientation == LinearLayoutManager.VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }
            spanCount = max(SPAN_COUNT, totalSpace / mColumnWidth)
            mColumnWidthChanged = false
        }

        super.onLayoutChildren(recycler, state)
    }
}