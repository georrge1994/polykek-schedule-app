package com.android.feature.schools.adapters

import android.content.Context
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.android.feature.schools.R

/**
 * School item view.
 *
 * @constructor Create [SchoolItemView]
 *
 * @param context Context
 */
internal class SchoolItemView(context: Context) : AppCompatTextView(ContextThemeWrapper(context, R.style.BaseTextStyle)) {
    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            val horizontal = resources.getDimensionPixelSize(R.dimen.margin_item_big)
            val vertical = resources.getDimensionPixelSize(R.dimen.margin_item_small)
            setMargins(horizontal, vertical, horizontal, 0)
        }
        // Padding.
        val paddingVertical = resources.getDimensionPixelSize(R.dimen.item_layout_padding_vertical)
        val paddingHorizontal = resources.getDimensionPixelSize(R.dimen.item_layout_padding_horizontal)
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
        // Other.
        background = ResourcesCompat.getDrawable(resources, R.drawable.white_rounded_background, context.theme)
        elevation = resources.getDimensionPixelSize(R.dimen.app_elevation).toFloat()
        setLineSpacing(resources.getDimension(R.dimen.item_line_spacing_extra), 1.0f)
    }
}