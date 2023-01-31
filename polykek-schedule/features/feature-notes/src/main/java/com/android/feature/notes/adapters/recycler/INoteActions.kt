package com.android.feature.notes.adapters.recycler

/**
 * Note item actions promise to provide two actions: click and long pressed.
 */
internal interface INoteItemActions {
    /**
     * On click.
     *
     * @param position Position
     * @param noteId Note id
     */
    fun onClick(position: Int, noteId: String)

    /**
     * Long press.
     *
     * @param position Position
     * @param noteId Note id
     */
    fun longPress(position: Int, noteId: String)
}