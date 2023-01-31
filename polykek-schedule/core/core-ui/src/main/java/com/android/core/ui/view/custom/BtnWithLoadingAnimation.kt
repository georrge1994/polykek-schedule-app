package com.android.core.ui.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.android.core.ui.R
import com.google.android.material.button.MaterialButton

/**
 * Button with loading animation.
 *
 * @constructor Create [BtnWithLoadingAnimation]
 *
 * @param context Context object
 * @param attrs View attributes
 * @param defStyleAttr Default style attributes
 */
class BtnWithLoadingAnimation(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : FrameLayout(
    context,
    attrs,
    defStyleAttr
) {
    private var button: MaterialButton? = null
    private var progressAnimation: ProgressBar? = null
    private var buttonText: String? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.text)).apply {
            buttonText = getString(0).toString()
            recycle()
        }
        button = getMaterialButton()
        progressAnimation = getProgressBar(attrs)
        addView(button)
        addView(progressAnimation)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        button?.setOnClickListener(l)
    }

    /**
     * Get material button.
     *
     * @return [MaterialButton]
     */
    private fun getMaterialButton() = MaterialButton(ContextThemeWrapper(context, R.style.Feedback_Button)).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        text = buttonText
        isVisible = true
    }

    /**
     * Get progress bar.
     *
     * @return [ProgressBar]
     */
    private fun getProgressBar(attrs: AttributeSet?) = ProgressBar(context, attrs, android.R.attr.progressBarStyleLarge).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT,
            Gravity.CENTER
        ).apply {
            setPadding(resources.getDimensionPixelSize(R.dimen.padding_big))
            isIndeterminate = true
            isVisible = false
        }
    }

    /**
     * Change animation state.
     *
     * @param isLoading Is loading
     */
    fun changeAnimationState(isLoading: Boolean) {
        button?.isEnabled = !isLoading
        progressAnimation?.isVisible = isLoading
        button?.text = if (isLoading) "" else buttonText
    }
}