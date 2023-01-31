package com.android.feature.schedule.base.adapters

import android.content.Context
import android.content.res.Configuration
import androidx.recyclerview.widget.RecyclerView
import com.android.common.models.schedule.Lesson

/**
 * Lesson view holder.
 *
 * @property lessonItemView [LessonItemPortraitView]
 * @property listener Item action listener
 * @constructor Create [LessonViewHolder]
 */
internal class LessonViewHolder private constructor(
    private val lessonItemView: LessonItemBaseView,
    private val listener: ILessonActions?
) : RecyclerView.ViewHolder(lessonItemView) {
    /**
     * Bind to.
     *
     * @param lesson Lesson
     */
    internal fun bindTo(lesson: Lesson) = lessonItemView.bindData(lesson, listener)

    companion object {
        /**
         * On create.
         *
         * @param context Context
         * @param listener Listener
         * @return [LessonViewHolder]
         */
        internal fun onCreate(
            context: Context,
            listener: ILessonActions?
        ): LessonViewHolder = LessonViewHolder(
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                LessonItemPortraitView(context)
            } else {
                LessonItemLandView(context)
            }, listener
        )
    }
}