package com.android.feature.notes.useCases

import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.notes.Note
import com.android.feature.notes.adapters.recycler.NoteItem
import com.android.feature.notes.models.NotesTabType
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.unitTest.BaseUnitTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Notes use case test for [NotesUseCase].
 *
 * @constructor Create empty constructor for notes use case test
 */
class NotesUseCaseTest : BaseUnitTest() {
    private val notesRoomRepository: INotesRoomRepository = mockk {
        coEvery { getNotesWhichContains("_own_note_", any()) } returns listOf(
            Note(id = "_own_note_2", name = "note2", header = "title2", body = "body2")
        )
        coEvery { getNotesWhichContains("1", any()) } returns listOf(
            Note(id = "1", name = "note1", header = "title1", body = "body1"),
        )
        coEvery { deleteNotesByIds(any()) } returns Unit
    }
    private val notesUseCase = NotesUseCase(notesRoomRepository)

    private val notesMock = mapOf(
        NotesTabType.BY_LESSONS to listOf(NoteItem(Note(id = "1", name = "note1", header = "title1", body = "body1"))),
        NotesTabType.OWN_NOTES to listOf(
            NoteItem(
                Note(
                    id = "_own_note_2",
                    name = "note2",
                    header = "title2",
                    body = "body2"
                )
            )
        ),
    )

    /**
     * Get notes.
     */
    @Test
    fun getNotes() = runBlockingUnit {
        assertEquals(
            notesMock,
            notesUseCase.getNotes(1, "1")
        )
        coVerify(exactly = 2) { notesRoomRepository.getNotesWhichContains(any(), any()) }
    }

    /**
     * Get notes.
     */
    @Test
    fun deleteNotesByIds() = runBlockingUnit {
        notesUseCase.deleteNotesByIds(HashSet())
        coVerify(exactly = 1) { notesRoomRepository.deleteNotesByIds(any()) }
    }
}