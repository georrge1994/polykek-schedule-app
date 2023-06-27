package com.android.feature.notes.viewModels

import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.notes.Note
import com.android.feature.notes.mvi.NoteEditorIntent
import com.android.feature.notes.mvi.NoteEditorState
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Note editor view model test for [NoteEditorViewModel].
 *
 * @constructor Create empty constructor for note editor view model test
 */
class NoteEditorViewModelTest : BaseViewModelUnitTest() {
    private val noteMock: Note = Note(
        id = "0",
        name = "name",
        header = "header",
        body = "body"
    )
    private val notesRoomRepository: INotesRoomRepository = mockk {
        coEvery { getNoteById(any()) } returns noteMock
        coEvery { insert(any()) } returns Unit
        coEvery { deleteNoteById(any()) } returns 1
    }
    private val noteEditorViewModel = NoteEditorViewModel(application, notesRoomRepository)

    /**
     * Complex test.
     */
    @Test
    fun complexTest() = runBlockingUnit {
        // Default.
        noteEditorViewModel.state.value.compareState(null, null, null, null)

        // Init content.
        val initContentIntent = NoteEditorIntent.InitContent(null, null, null)
        noteEditorViewModel.dispatchIntentAsync(initContentIntent).joinWithTimeout()
        noteEditorViewModel.state.value.compareState(noteMock.id, noteMock.name, noteMock.header, noteMock.body)
        coVerify(exactly = 1) { notesRoomRepository.getNoteById(any()) }

        noteEditorViewModel.dispatchIntentAsync(NoteEditorIntent.SaveHeader("newHeader")).joinWithTimeout()
        noteEditorViewModel.dispatchIntentAsync(NoteEditorIntent.SaveBody("newBody")).joinWithTimeout()

        // Update.
        noteEditorViewModel.dispatchIntentAsync(NoteEditorIntent.UpdateNote).joinWithTimeout()
        coVerify(exactly = 1) {
            notesRoomRepository.insert(
                Note(
                    id = "0",
                    name = "name",
                    header = "newHeader",
                    body = "newBody"
                )
            )
        }
        // Delete.
        noteEditorViewModel.dispatchIntentAsync(NoteEditorIntent.DeleteNote).joinWithTimeout()
        coVerify(exactly = 1) { notesRoomRepository.deleteNoteById(noteMock.id) }
    }

    /**
     * Update note deleting blank.
     */
    @Test
    fun updateNote_deletingBlank() = runBlockingUnit {
        coEvery { notesRoomRepository.getNoteById(any()) } returns null
        val initContentIntent = NoteEditorIntent.InitContent("0", null, null)
        noteEditorViewModel.dispatchIntentAsync(initContentIntent).joinWithTimeout()
        noteEditorViewModel.state.value.compareState(noteMock.id, "test string", "", "")
        coVerify(exactly = 1) { notesRoomRepository.getNoteById(any()) }
        // Update.
        noteEditorViewModel.dispatchIntentAsync(NoteEditorIntent.UpdateNote).joinWithTimeout()
        coVerify(exactly = 1) { notesRoomRepository.deleteNoteById("0") }
    }

    /**
     * Compare state.
     *
     * @receiver [NoteEditorState]
     * @param id Id
     * @param name Name
     * @param header Header
     * @param body Body
     */
    private fun NoteEditorState.compareState(
        id: String?,
        name: String?,
        header: String?,
        body: String?
    ) {
        assertEquals(id, this.note?.id)
        assertEquals(name, this.note?.name)
        assertEquals(header, this.note?.header)
        assertEquals(body, this.note?.body)
    }
}