package argument.twins.com.polykekschedule.room.notes

import argument.twins.com.polykekschedule.dagger.core.DATABASE_DISPATCHER
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.notes.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Notes room repository.
 *
 * @property notesDao Contains entities wih users` notes
 * @property databaseDispatcher Single thread dispatcher to synchronize all requests to avoid concurrency exception
 * @constructor Create [NotesRoomRepository]
 */
class NotesRoomRepository @Inject constructor(
    private val notesDao: NotesDao,
    @Named(DATABASE_DISPATCHER) private val databaseDispatcher: CoroutineDispatcher
) : INotesRoomRepository {
    override suspend fun getNotesWhichContains(noteIdBit: String, keyWord: String): List<Note> =
        withContext(databaseDispatcher) {
            notesDao.getNotesWhichContains(noteIdBit, keyWord).map { entity ->
                entity.toNote()
            }
        }

    override suspend fun getNoteIds(): List<String> = withContext(databaseDispatcher) {
        notesDao.getNoteIds()
    }

    override fun getNoteIdsFlow(): Flow<List<String>?> = notesDao.getNoteIdsFlow()

    override suspend fun getNoteById(noteId: String): Note? = withContext(databaseDispatcher) {
        notesDao.getNoteById(noteId)?.toNote()
    }

    override suspend fun insert(note: Note) = withContext(databaseDispatcher) {
        notesDao.insert(note.toEntity())
    }

    override suspend fun deleteNotesByIds(noteIds: Collection<String>) = withContext(databaseDispatcher) {
        notesDao.deleteNotes(noteIds)
    }

    override suspend fun deleteNoteById(noteId: String): Int = withContext(databaseDispatcher) {
        notesDao.deleteNoteById(noteId)
    }

    /**
     * To note.
     *
     * @receiver [NoteEntity]
     * @return [Note]
     */
    private fun NoteEntity.toNote() = Note(
        id = id,
        name = name,
        header = header,
        body = body
    )

    /**
     * To entity.
     *
     * @receiver [Note]
     * @return [NoteEntity]
     */
    private fun Note.toEntity() = NoteEntity(
        id = id,
        name = name,
        header = header,
        body = body
    )
}