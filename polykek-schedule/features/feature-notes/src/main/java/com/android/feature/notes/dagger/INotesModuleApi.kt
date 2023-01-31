package com.android.feature.notes.dagger

import androidx.fragment.app.Fragment
import com.android.module.injector.moduleMarkers.IModuleApi

/**
 * Notes api module.
 */
interface INotesModuleApi : IModuleApi {
    /**
     * Get faq screen.
     *
     * @return [Fragment]
     */
    fun getNotesFragment(): Fragment

    /**
     * Get note editor fragment.
     *
     * @param noteId Note id
     * @param title Title
     * @return [Fragment]
     */
    fun getNoteEditorFragment(noteId: String, title: String): Fragment

    /**
     * Get note editor fragment.
     *
     * @param selectedItem Selected item
     * @return [Fragment]
     */
    fun getNoteEditorFragment(selectedItem: Int): Fragment

    /**
     * Get note editor fragment.
     *
     * @param ownNoteId Own note id
     * @return [Fragment]
     */
    fun getNoteEditorFragment(ownNoteId: String): Fragment
}