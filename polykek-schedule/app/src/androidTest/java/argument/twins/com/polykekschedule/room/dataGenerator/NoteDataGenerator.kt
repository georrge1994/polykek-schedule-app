package argument.twins.com.polykekschedule.room.dataGenerator

import com.android.core.room.api.notes.Note
import com.android.test.support.testFixtures.TestDataGenerator

/**
 * Notes data generator for mockk tests.
 */
class NoteDataGenerator : TestDataGenerator {
    val noteMock1: Note = Note(
        id = "1",
        name = "name1",
        header = "header1",
        body = "body1"
    )
    val noteMock2: Note = Note(
        id = "2",
        name = "name2",
        header = "header2",
        body = "body2"
    )
    val ownNoteMock1: Note = Note(
        id = "1_own_note_",
        name = "name1",
        header = "header1",
        body = "body1"
    )
    val ownNoteMock2: Note = Note(
        id = "2_own_note_",
        name = "name2",
        header = "header2",
        body = "body2"
    )
}