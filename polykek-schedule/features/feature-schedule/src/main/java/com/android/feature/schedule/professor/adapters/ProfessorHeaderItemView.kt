package com.android.feature.schedule.professor.adapters

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.android.feature.schedule.R

/**
 * The header item for professor schedule. Used to avoid inflating process on the fly.
 *
 * @constructor Create [ProfessorHeaderItemView]
 *
 * @param context Context
 */
internal class ProfessorHeaderItemView(context: Context) : AppCompatTextView(ContextThemeWrapper(context, R.style.Bold_Label_Style)) {
    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            val horizontal = resources.getDimensionPixelSize(R.dimen.margin_item_big)
            val top = resources.getDimensionPixelSize(R.dimen.margin_small)
            setMargins(horizontal, top, horizontal, 0)
        }
        // Padding.
        val padding = resources.getDimensionPixelSize(R.dimen.padding_middle)
        setPadding(padding, padding, padding, 0)
        // Other.
        setTextColor(ContextCompat.getColor(context, R.color.grey_200))
        setLineSpacing(resources.getDimension(R.dimen.item_line_spacing_extra), 1.0f)
        gravity = Gravity.START
    }
}