package com.android.professors.base.adapters

import android.content.Context
import android.view.ContextThemeWrapper
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.android.core.ui.view.custom.getJustifiedTextView
import com.android.core.ui.view.ext.setRandomViewId
import com.android.feature.professors.R

/**
 * Professor item view. Used to avoid inflating process on the fly.
 *
 * @constructor Create [ProfessorItemView]
 *
 * @param context Context
 */
internal class ProfessorItemView(context: Context) : ConstraintLayout(context) {
    val parentLayout: ConstraintLayout = this.setRandomViewId()
    val name: TextView = TextView(ContextThemeWrapper(context, R.style.BaseTextStyle)).setRandomViewId()
    val chair: TextView = getJustifiedTextView(context, R.style.Small_Label_Style).setRandomViewId()
    val enterArrow: ImageView = ImageView(context).setRandomViewId()

    init {
        initParentLayout()
        initNameTextView()
        initChairTextView()
        initEnterArrowImageView()
    }

    /**
     * Init parent layout.
     */
    private fun initParentLayout() = with(parentLayout) {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            val horizontal = resources.getDimensionPixelSize(R.dimen.margin_item_big)
            val vertical = resources.getDimensionPixelSize(R.dimen.margin_item_small)
            setMargins(horizontal, vertical, horizontal, 0)
        }
        val padding = resources.getDimensionPixelSize(R.dimen.padding_item_small)
        setPadding(padding, padding, padding, padding)
        background = ResourcesCompat.getDrawable(resources, R.drawable.white_rounded_background, context.theme)
        elevation = resources.getDimensionPixelSize(R.dimen.app_elevation).toFloat()
        addView(name)
        addView(chair)
        addView(enterArrow)
    }

    /**
     * Init name.
     */
    private fun initNameTextView() = with(name) {
        layoutParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT).apply {
            val marginStart = resources.getDimensionPixelSize(R.dimen.margin_big)
            val marginBottom = resources.getDimensionPixelSize(R.dimen.margin_item_middle)
            val marginOther = resources.getDimensionPixelSize(R.dimen.margin_small)
            setMargins(marginStart, marginOther, marginOther, marginBottom)
            bottomToTop = chair.id
            endToStart = enterArrow.id
            startToStart = parentLayout.id
            topToTop = parentLayout.id
        }
    }

    /**
     * Init chair.
     */
    private fun initChairTextView() = with(chair) {
        layoutParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT).apply {
            val marginBottom = resources.getDimensionPixelSize(R.dimen.margin_small)
            setMargins(0, 0, 0, marginBottom)
            bottomToBottom = parentLayout.id
            endToEnd = name.id
            startToStart = name.id
            topToBottom = name.id
        }
    }

    /**
     * Init enter arrow.
     */
    private fun initEnterArrowImageView() = with(enterArrow) {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            val endMargin = resources.getDimensionPixelSize(R.dimen.margin_small)
            setMargins(0, 0, endMargin, 0)
            bottomToBottom = chair.id
            endToEnd = parentLayout.id
            topToTop = name.id
        }
        background = ContextCompat.getDrawable(context, R.drawable.ic_baseline_arrow_forward_ios_24)
    }
}