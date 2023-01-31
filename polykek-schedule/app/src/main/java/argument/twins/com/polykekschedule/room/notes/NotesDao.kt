package argument.twins.com.polykekschedule.room.notes

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Notes dao. Work via [NotesRoomRepository].
 */
@Dao
interface NotesDao {
    @Query(
        "SELECT * from NOTES WHERE id LIKE '%' || :noteIdBit || '%' AND (" +
                "title LIKE '%' || :keyWord || '%' OR " +
                "name LIKE '%' || :keyWord || '%' OR " +
                "body LIKE '%' || :keyWord || '%' " +
                ") ORDER BY id ASC, title ASC, body ASC "
    )
    fun getNotesWhichContains(noteIdBit: String, keyWord: String): List<NoteEntity>

    @Query("SELECT id FROM NOTES")
    fun getNoteIds(): List<String>

    @Query("SELECT id FROM NOTES")
    fun getNoteIdsFlow(): Flow<List<String>>

    @Query("SELECT * FROM NOTES WHERE id = :noteId")
    fun getNoteById(noteId: String): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: NoteEntity)

    @Query("DELETE FROM NOTES WHERE id IN (:noteIds)")
    fun deleteNotes(noteIds: Collection<String>)

    @Query("DELETE FROM NOTES WHERE id = :noteId")
    fun deleteNoteById(noteId: String): Int
}