package com.android.schedule.controller.impl.dagger

import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import retrofit2.Retrofit

/**
 * Dependencies for schedule controller.
 */
interface IScheduleControllerModuleDependencies : ICoreUiModuleDependencies {
    val retrofit: Retrofit
    val notesRoomRepository: INotesRoomRepository
    val savedItemsRoomRepository: ISavedItemsRoomRepository
}