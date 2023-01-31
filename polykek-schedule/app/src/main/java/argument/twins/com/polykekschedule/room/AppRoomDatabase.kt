package argument.twins.com.polykekschedule.room

import androidx.room.Database
import androidx.room.RoomDatabase
import argument.twins.com.polykekschedule.room.notes.NoteEntity
import argument.twins.com.polykekschedule.room.notes.NotesDao
import argument.twins.com.polykekschedule.room.savedItems.SavedItemEntity
import argument.twins.com.polykekschedule.room.savedItems.SavedItemsDao

private const val DATABASE_VERSION = 3

@Database(
    entities = [
        NoteEntity::class,
        SavedItemEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = true
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    abstract fun savedItemsDao(): SavedItemsDao
}