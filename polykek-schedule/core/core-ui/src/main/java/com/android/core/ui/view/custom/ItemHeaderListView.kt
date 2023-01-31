package com.android.core.ui.view.custom

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import com.android.core.ui.R

/**
 * Header item view for school and group lists.
 *
 * @constructor Create [ItemHeaderListView]
 *
 * @param context Context
 */
class ItemHeaderListView(context: Context) : AppCompatTextView(ContextThemeWrapper(context, R.style.Bold_Label_Style)) {
    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val paddingVertical = resources.getDimensionPixelSize(R.dimen.item_layout_padding_vertical)
        val paddingHorizontal = resources.getDimensionPixelSize(R.dimen.padding_small)
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
        gravity = Gravity.START
        setLineSpacing(resources.getDimension(R.dimen.item_line_spacing_extra), 1.0f)
    }
}