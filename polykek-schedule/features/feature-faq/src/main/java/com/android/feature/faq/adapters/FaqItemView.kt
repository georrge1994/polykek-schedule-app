package com.android.feature.faq.adapters

import android.content.Context
import android.view.ContextThemeWrapper
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.android.core.ui.view.ext.setRandomViewId
import com.android.feature.faq.R
import com.android.core.ui.view.custom.getJustifiedTextView

/**
 * FAQ item view. Used to avoid inflating process on the fly.
 *
 * @constructor Create [FaqItemView]
 *
 * @param context Context
 */
internal class FaqItemView(context: Context) : ConstraintLayout(context) {
    val parentLayout: ConstraintLayout = this.setRandomViewId()
    val question: TextView = TextView(ContextThemeWrapper(context, R.style.BaseTextStyle)).setRandomViewId()
    val answer: TextView = getJustifiedTextView(context, R.style.Small_Label_Style).setRandomViewId()
    val moreIcon: AppCompatImageView = AppCompatImageView(context).setRandomViewId()

    init {
        initParentLayout()
        initQuestionTextView()
        initAnswerTextView()
        initMoreIcon()
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
        val padding = resources.getDimensionPixelSize(R.dimen.padding_tiny)
        setPadding(padding, padding, padding, padding)
        background = ResourcesCompat.getDrawable(resources, R.drawable.white_rounded_background, context.theme)
        elevation = resources.getDimensionPixelSize(R.dimen.app_elevation).toFloat()
        addView(question)
        addView(answer)
        addView(moreIcon)
    }

    /**
     * Init question text view.
     */
    private fun initQuestionTextView() = with(question) {
        layoutParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT).apply {
            val margin = resources.getDimensionPixelSize(R.dimen.margin_small)
            setMargins(margin, margin, margin, margin)
            startToStart = parentLayout.id
            topToTop = parentLayout.id
            endToStart = moreIcon.id
            bottomToTop = answer.id
        }
    }

    /**
     * Init answer text view.
     */
    private fun initAnswerTextView() = with(answer) {
        layoutParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT).apply {
            val margin = resources.getDimensionPixelSize(R.dimen.margin_small)
            setMargins(margin, 0, margin, margin)
            startToStart = parentLayout.id
            topToBottom = question.id
            endToEnd = parentLayout.id
            bottomToBottom = parentLayout.id
        }
    }

    /**
     * Init more icon.
     */
    private fun initMoreIcon() = with(moreIcon) {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            val endMargin = resources.getDimensionPixelSize(R.dimen.margin_item_big)
            setMargins(0, 0, endMargin, 0)
            topToTop = question.id
            endToEnd = parentLayout.id
        }
        background = ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_keyboard_arrow_down_24, context.theme)
    }
}