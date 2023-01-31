package argument.twins.com.polykekschedule.room.notes

import argument.twins.com.polykekschedule.room.AppRoomDatabase
import argument.twins.com.polykekschedule.room.dataGenerator.NoteDataGenerator
import com.android.test.support.androidTest.base.BaseRoomRepositoryTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Notes room repository test for [NotesRoomRepository].
 *
 * @constructor Create empty constructor for notes room repository test
 */
class NotesRoomRepositoryTest : BaseRoomRepositoryTest<AppRoomDatabase>(AppRoomDatabase::class) {
    private val noteDataGenerator = NoteDataGenerator()
    private lateinit var notesRoomRepository: NotesRoomRepository

    override fun beforeTest() {
        super.beforeTest()
        notesRoomRepository = NotesRoomRepository(
            notesDao = db.notesDao(),
            databaseDispatcher = unconfinedTestDispatcher
        )
    }

    /**
     * Get notes which contains key.
     */
    @Test
    fun getNotesWhichContains() = runTest {
        insertTestData()
        val result = notesRoomRepository.getNotesWhichContains(noteIdBit = "_own_note_", keyWord = "name1")
        assertEquals(1, result.size)
        assertEquals(noteDataGenerator.ownNoteMock1, result.first())
    }

    /**
     * Get note ids.
     */
    @Test
    fun getNoteIds() = runTest {
        insertTestData()
        assertEquals(listOf("1", "1_own_note_", "2", "2_own_note_"), notesRoomRepository.getNoteIds())
    }

    /**
     * Get note ids flow.
     */
    @Test
    fun getNoteIdsFlow() = runTest {
        insertTestData()
        notesRoomRepository.getNoteIdsFlow().subscribeAndCompareFirstValue(listOf("1", "1_own_note_", "2", "2_own_note_"))
    }

    /**
     * Delete note by id.
     */
    @Test
    fun deleteNoteById() = runTest {
        insertTestData()
        notesRoomRepository.deleteNoteById(noteDataGenerator.noteMock1.id)
        assertEquals(null, notesRoomRepository.getNoteById(noteDataGenerator.noteMock1.id))
    }

    /**
     * Delete notes by ids.
     */
    @Test
    fun deleteNotesByIds() = runTest {
        insertTestData()
        notesRoomRepository.deleteNotesByIds(listOf(noteDataGenerator.noteMock1.id, noteDataGenerator.noteMock2.id))
        assertEquals(null, notesRoomRepository.getNoteById(noteDataGenerator.noteMock1.id))
        assertEquals(null, notesRoomRepository.getNoteById(noteDataGenerator.noteMock2.id))
    }

    /**
     * Get note by id.
     */
    @Test
    fun getNoteById() = runTest {
        insertTestData()
        assertEquals(noteDataGenerator.noteMock1, notesRoomRepository.getNoteById(noteDataGenerator.noteMock1.id))
    }

    /**
     * Insert test data.
     */
    private suspend fun insertTestData() {
        notesRoomRepository.insert(note = noteDataGenerator.noteMock1)
        notesRoomRepository.insert(note = noteDataGenerator.noteMock2)
        notesRoomRepository.insert(note = noteDataGenerator.ownNoteMock1)
        notesRoomRepository.insert(note = noteDataGenerator.ownNoteMock2)
    }
}