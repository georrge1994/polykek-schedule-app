package com.android.feature.main.screen.saved.adapters

import com.android.common.models.savedItems.SavedItem

/**
 * Actions with bottom menu.
 */
internal interface ISaveItemMenuActions {
    /**
     * On click (select this item).
     *
     * @param item Item
     */
    fun onClick(item: SavedItem)

    /**
     * On remove (remove group or professor item, but not the last).
     *
     * @param item Item
     */
    fun onRemove(item: SavedItem)

    /**
     * Add group (open screen with schools/groups selection).
     */
    fun addGroup()

    /**
     * Add professor (open screen with professors search).
     */
    fun addProfessor()

    /**
     * Say about schedule error (open email app by action).
     */
    fun sayAboutScheduleError()
}