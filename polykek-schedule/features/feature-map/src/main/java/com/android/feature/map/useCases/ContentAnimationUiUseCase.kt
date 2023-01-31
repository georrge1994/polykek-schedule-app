package com.android.feature.map.useCases

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.res.Configuration
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import com.android.shared.code.utils.markers.IUseCase
import javax.inject.Inject

/**
 * Content animation ui use case provides animation for Yandex-map toolbar item.
 *
 * @constructor Create empty constructor for content animation ui use case
 */
internal class ContentAnimationUiUseCase @Inject constructor() : IUseCase {
    /**
     * Show content.
     *
     * @param viewGroup View group
     * @param childView Child view
     */
    internal fun showContent(viewGroup: ViewGroup, childView: View) {
        TransitionManager.beginDelayedTransition(viewGroup)
        childView.visibility = View.VISIBLE
    }

    /**
     * Hide content.
     *
     * @param viewGroup View group
     * @param childView Child view
     */
    internal fun hideContent(
        viewGroup: ViewGroup,
        childView: View
    ) = if (viewGroup.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        // Default portrait animation is glitching, so use custom solution.
        childView.hideWithHelpCodeAnimation()
    } else {
        TransitionManager.beginDelayedTransition(viewGroup)
        childView.visibility = View.GONE
    }

    /**
     * Hide with help code animation.
     *
     * @receiver [View]
     */
    private fun View.hideWithHelpCodeAnimation() = ValueAnimator.ofInt(height, 0).apply {
        val wrapContentHeight = height
        duration = 250L
        addUpdateListener {
            val animatedValue = animatedValue as Int
            changeLayoutParams(animatedValue)
            alpha = animatedValue.toFloat() / wrapContentHeight
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                changeLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT)
                this@hideWithHelpCodeAnimation.visibility = View.GONE
                this@hideWithHelpCodeAnimation.alpha = 1f
            }
        })
    }.start()

    /**
     * Change layout params.
     *
     * @receiver [View]
     * @param newHeight New height
     */
    private fun View.changeLayoutParams(newHeight: Int) {
        val layoutParams = this.layoutParams
        this.layoutParams.height = newHeight
        this.layoutParams = layoutParams
    }
}