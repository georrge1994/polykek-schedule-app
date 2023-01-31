package com.android.feature.schedule.base.dagger

import androidx.fragment.app.Fragment

/**
 * Schedule navigation actions.
 */
interface IScheduleNavigationActions {
    /**
     * Get note editor.
     *
     * @return [Fragment]
     */
    fun getNoteEditorFragment(noteId: String, title: String): Fragment
}