package com.android.feature.schedule.base.adapters

import com.android.common.models.schedule.Lesson

/**
 * Lesson actions.
 */
internal interface ILessonActions {
    /**
     * On click.
     *
     * @param lesson Lesson
     */
    fun onClick(lesson: Lesson)
}