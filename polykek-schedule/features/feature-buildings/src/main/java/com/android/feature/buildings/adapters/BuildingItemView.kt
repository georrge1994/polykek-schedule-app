package com.android.feature.buildings.adapters

import android.content.Context
import android.view.ContextThemeWrapper
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import com.android.core.ui.view.ext.setRandomViewId
import com.android.feature.buildings.R

/**
 * Building item view. Used to avoid inflating process.
 *
 * @constructor Create [BuildingItemView]
 *
 * @param context Context
 */
internal class BuildingItemView(context: Context) : LinearLayoutCompat(context) {
    val parentLayout = this.setRandomViewId()
    val name = TextView(ContextThemeWrapper(context, R.style.BaseTextStyle)).setRandomViewId()
    val address = TextView(ContextThemeWrapper(context, R.style.Small_Label_Style)).setRandomViewId()

    init {
        initParentLayout()
        initNameTextView()
        initAddressTextView()
    }

    /**
     * Init parent layout.
     */
    private fun initParentLayout() = with(parentLayout) {
        orientation = VERTICAL
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            val horizontal = resources.getDimensionPixelSize(R.dimen.margin_item_big)
            val vertical = resources.getDimensionPixelSize(R.dimen.margin_item_small)
            setMargins(horizontal, vertical, horizontal, 0)
        }
        val padding = resources.getDimensionPixelSize(R.dimen.padding_tiny)
        setPadding(padding, padding, padding, padding)
        background = ResourcesCompat.getDrawable(resources, R.drawable.white_rounded_background, context.theme)
        elevation = resources.getDimensionPixelSize(R.dimen.app_elevation).toFloat()
        addView(name)
        addView(address)
    }

    /**
     * Init text view.
     */
    private fun initNameTextView() = with(name) {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            val horizontal = resources.getDimensionPixelSize(R.dimen.margin_middle)
            val vertical = resources.getDimensionPixelSize(R.dimen.margin_item_big)
            setMargins(horizontal, vertical, horizontal, vertical)
        }
    }

    /**
     * Init address text view.
     */
    private fun initAddressTextView() = with(address) {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            val horizontal = resources.getDimensionPixelSize(R.dimen.margin_middle)
            val vertical = resources.getDimensionPixelSize(R.dimen.margin_small)
            setMargins(horizontal, 0, horizontal, vertical)
        }
    }
}