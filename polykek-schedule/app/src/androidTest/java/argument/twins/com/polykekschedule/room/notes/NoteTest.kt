package argument.twins.com.polykekschedule.room.notes

import com.android.core.room.api.notes.Note
import com.android.test.support.unitTest.BaseUnitTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Note test for [Note].
 *
 * @constructor Create empty constructor for note test
 */
class NoteTest : BaseUnitTest() {
    /**
     * Is not blank note.
     */
    @Test
    fun isNotBlankNote() {
        assertFalse(
            Note(
                id = "id",
                name = "name",
                header = "header",
                body = "body"
            ).isBlank()
        )
    }

    /**
     * Is blank note.
     */
    @Test
    fun isBlankNote() {
        assertTrue(
            Note(
                id = "id",
                name = "name",
                header = "",
                body = ""
            ).isBlank()
        )
    }
}