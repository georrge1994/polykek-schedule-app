package com.android.core.ui.view.ext

import android.view.View
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

/**
 * Generate and set random id to the view.
 *
 * @receiver [T]
 * @param T Type of view
 * @return [T]
 */
fun <T : View> T.setRandomViewId(): T {
    id = ConstraintLayout.generateViewId()
    return this
}

/**
 * Set visible or invisible state to the view.
 *
 * @receiver [View]
 * @param isVisible Is visible
 */
fun View.isVisibleOrInvisible(isVisible: Boolean) {
    visibility = if (isVisible)
        View.VISIBLE
    else
        View.INVISIBLE
}

/**
 * Set divider.
 *
 * @receiver [RecyclerView]
 * @param drawableRes Drawable res
 */
fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    ContextCompat.getDrawable(context, drawableRes)?.let { drawable ->
        DividerItemDecoration(context, DividerItemDecoration.VERTICAL).let { divider ->
            divider.setDrawable(drawable)
            addItemDecoration(divider)
        }
    }
}