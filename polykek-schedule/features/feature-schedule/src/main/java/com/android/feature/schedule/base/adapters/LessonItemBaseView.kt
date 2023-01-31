package com.android.feature.schedule.base.adapters

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.common.models.schedule.Lesson
import com.android.core.ui.view.ext.setRandomViewId

/**
 * Lesson item view. Used to avoid inflating process on the fly.
 *
 * @constructor Create [LessonItemBaseView]
 *
 * @param context Context
 */
internal abstract class LessonItemBaseView(context: Context) : ConstraintLayout(context) {
    protected val parentLayout: ConstraintLayout = this.setRandomViewId()

    /**
     * Bind data.
     *
     * @param lesson Lesson
     */
    internal abstract fun bindData(lesson: Lesson, listener: ILessonActions?)

    /**
     * Add view to parent.
     *
     * @receiver [T]
     * @param T Type of view
     * @return [T]
     */
    protected fun <T : View> T.addToParent(): T {
        id = generateViewId()
        this@LessonItemBaseView.addView(this)
        return this
    }
}