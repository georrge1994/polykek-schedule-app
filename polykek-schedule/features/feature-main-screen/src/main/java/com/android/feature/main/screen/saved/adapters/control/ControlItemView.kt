package com.android.feature.main.screen.saved.adapters.control

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.android.feature.main.screen.R

/**
 * Control item view. Used to avoid inflating process on the fly.
 *
 * @constructor Create [ControlItemView]
 *
 * @param context Context
 */
internal class ControlItemView(context: Context) : AppCompatButton(ContextThemeWrapper(context, R.style.ControlButton)) {
    init {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        background = ContextCompat.getDrawable(context, R.drawable.selector_shadow)
        compoundDrawablePadding = resources.getDimensionPixelSize(R.dimen.padding_small)
        setTextColor(ContextCompat.getColor(context, R.color.grey_200))
        gravity = Gravity.START or Gravity.CENTER_VERTICAL
        isAllCaps = false
    }
}