package com.android.feature.main.screen.saved.adapters.savedItem

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.android.core.ui.view.ext.setRandomViewId
import com.android.feature.main.screen.R

/**
 * Saved item view. Used to avoid inflating process on the fly.
 *
 * @constructor Create [SavedItemView]
 *
 * @param context Context
 */
internal class SavedItemView(context: Context) : FrameLayout(context) {
    val parentLayout: FrameLayout = this.setRandomViewId()
    val title: AppCompatTextView = AppCompatTextView(ContextThemeWrapper(context, R.style.BaseTextStyle)).setRandomViewId()
    val checked: AppCompatImageView = AppCompatImageView(context).setRandomViewId()

    init {
        initParentLayout()
        initTitleTextView()
        initCheckedImgView()
    }

    /**
     * Init parent layout.
     */
    private fun initParentLayout() = with(parentLayout) {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            val marginHorizontal = resources.getDimensionPixelSize(R.dimen.margin_item_big)
            val marginVertical = resources.getDimensionPixelSize(R.dimen.margin_item_small)
            setMargins(marginHorizontal, marginVertical, marginHorizontal, marginVertical)
        }
        background = ContextCompat.getDrawable(context, R.drawable.white_rounded_background)
        addView(title)
        addView(checked)
    }

    /**
     * Init title text view.
     */
    private fun initTitleTextView() = with(title) {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val paddingVertical = resources.getDimensionPixelSize(R.dimen.item_layout_padding_vertical)
        val paddingHorizontal = resources.getDimensionPixelSize(R.dimen.item_layout_padding_horizontal)
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
        elevation = resources.getDimensionPixelSize(R.dimen.app_elevation).toFloat()
        setLineSpacing(resources.getDimension(R.dimen.item_line_spacing_extra), 1.0f)
    }

    /**
     * Init checked img view.
     */
    private fun initCheckedImgView() = with(checked) {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            val endMargin = resources.getDimensionPixelSize(R.dimen.margin_big)
            setMargins(0, 0, endMargin, 0)
            gravity = Gravity.CENTER_VERTICAL or Gravity.END
        }
        elevation = resources.getDimensionPixelSize(R.dimen.saved_items_icon_elevation).toFloat()
        background = ContextCompat.getDrawable(context, R.drawable.ic_check_grey_24dp)
        visibility = View.GONE
    }
}