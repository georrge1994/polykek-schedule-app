package com.android.common.models.savedItems

/**
 * This item could be used for fetching schedule from Polytech servers. It could be group or professor.
 */
interface ISavedItem {
    /**
     * To saved item.
     *
     * @return [SavedItem]
     */
    fun toSavedItem(): SavedItem
}