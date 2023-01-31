package argument.twins.com.polykekschedule.dagger.core

import argument.twins.com.polykekschedule.room.notes.NotesRoomRepository
import argument.twins.com.polykekschedule.room.savedItems.SavedItemsRoomRepository
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import dagger.Binds
import dagger.Module

@Module
abstract class CoreRoomRepositoryBinder {
    @Binds
    abstract fun provideSavedItemsRoomRepository(
        savedItemsRoomRepository: SavedItemsRoomRepository
    ): ISavedItemsRoomRepository

    @Binds
    abstract fun provideNotesRoomRepository(
        notesRoomRepository: NotesRoomRepository
    ): INotesRoomRepository
}