package com.android.feature.notes.adapters.recycler

import android.content.Context
import android.text.InputFilter
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.android.core.ui.view.ext.setRandomViewId
import com.android.feature.notes.R
import com.codesgood.views.JustifiedTextView

/**
 * Note item view. Used to avoid inflating process on the fly.
 *
 * @constructor Create [NoteItemView]
 *
 * @param context Context
 */
internal class NoteItemView(context: Context) : ConstraintLayout(context) {
    val parentLayout: ConstraintLayout = this.setRandomViewId()
    val name: TextView = TextView(ContextThemeWrapper(context, R.style.Bold_Label_Style)).setRandomViewId()
    val title: TextView = JustifiedTextView(ContextThemeWrapper(context, R.style.Bold_Label_Style)).setRandomViewId()
    val body: TextView = JustifiedTextView(ContextThemeWrapper(context, R.style.Small_Label_Style)).setRandomViewId()
    val checkBox: CheckBox = CheckBox(context).setRandomViewId()

    init {
        initParentLayout()
        initNameTextView()
        initTitleTextView()
        initBodyTextView()
        initCheckBox()
    }

    /**
     * Init parent layout.
     */
    private fun initParentLayout() = with(parentLayout) {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            val horizontal = resources.getDimensionPixelSize(R.dimen.margin_item_big)
            val vertical = resources.getDimensionPixelSize(R.dimen.margin_item_small)
            setMargins(horizontal, vertical, horizontal, vertical)
        }
        val padding = resources.getDimensionPixelSize(R.dimen.padding_item_small)
        setPadding(padding, padding, padding, padding)
        background = ResourcesCompat.getDrawable(resources, R.drawable.white_rounded_background, context.theme)
        elevation = resources.getDimensionPixelSize(R.dimen.app_elevation).toFloat()
        addView(name)
        addView(title)
        addView(body)
        addView(checkBox)
    }

    /**
     * Init name text view.
     */
    private fun initNameTextView() = with(name) {
        layoutParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT).apply {
            bottomToTop = title.id
            endToEnd = parentLayout.id
            startToEnd = checkBox.id
            topToTop = parentLayout.id
        }
        val paddingVertical = resources.getDimensionPixelSize(R.dimen.padding_item_small)
        val paddingHorizontal = resources.getDimensionPixelSize(R.dimen.padding_small)
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
    }

    /**
     * Init title text view.
     */
    private fun initTitleTextView() = with(title) {
        layoutParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT).apply {
            bottomToTop = body.id
            endToEnd = parentLayout.id
            startToEnd = checkBox.id
            topToBottom = name.id
        }
        val paddingVertical = resources.getDimensionPixelSize(R.dimen.padding_item_small)
        val paddingHorizontal = resources.getDimensionPixelSize(R.dimen.padding_small)
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
        setTextColor(ContextCompat.getColor(context, R.color.grey_200))
        addFilter(InputFilter.LengthFilter(250))
        maxLines = 5
    }

    /**
     * Init body text view.
     */
    private fun initBodyTextView() = with(body) {
        layoutParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT).apply {
            bottomToBottom = parentLayout.id
            endToEnd = parentLayout.id
            startToEnd = checkBox.id
            topToBottom = title.id
        }
        val paddingVertical = resources.getDimensionPixelSize(R.dimen.padding_item_small)
        val paddingHorizontal = resources.getDimensionPixelSize(R.dimen.padding_small)
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
        setTextColor(ContextCompat.getColor(context, R.color.grey_200))
        addFilter(InputFilter.LengthFilter(500))
        maxLines = 10
    }

    /**
     * Add filter.
     *
     * @receiver [TextView]
     * @param filter Filter
     */
    private fun TextView.addFilter(filter: InputFilter) {
        filters = if (filters.isNullOrEmpty()) {
            arrayOf(filter)
        } else {
            filters.toMutableList().apply {
                removeAll { it.javaClass == filter.javaClass }
                add(filter)
            }.toTypedArray()
        }
    }

    /**
     * Init check box.
     */
    private fun initCheckBox() = with(checkBox) {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            val startMargin = resources.getDimensionPixelSize(R.dimen.margin_small)
            setMargins(startMargin, 0, 0, 0)
            bottomToBottom = parentLayout.id
            startToStart = parentLayout.id
            topToTop = parentLayout.id
        }
        backgroundTintList = AppCompatResources.getColorStateList(context, R.color.colorPrimary)
        buttonDrawable = ContextCompat.getDrawable(context, R.drawable.custom_checkbox)
        visibility = View.GONE
        isClickable = false
        isFocusable = false
    }
}