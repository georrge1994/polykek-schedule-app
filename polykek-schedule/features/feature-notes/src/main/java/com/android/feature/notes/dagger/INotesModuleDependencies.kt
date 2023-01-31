package com.android.feature.notes.dagger

import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.ICoreUiModuleDependencies

/**
 * Notes module dependencies.
 */
interface INotesModuleDependencies : ICoreUiModuleDependencies {
    val notesRoomRepository: INotesRoomRepository
    val savedItemsRoomRepository: ISavedItemsRoomRepository
}