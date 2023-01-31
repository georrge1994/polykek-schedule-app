package com.android.feature.notes.viewModels

import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.notes.Note
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.getOrAwaitValue
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
        noteEditorViewModel.init(null, null, null)
        noteEditorViewModel.noteLiveData.getOrAwaitValue(noteMock)
        coVerify(exactly = 1) { notesRoomRepository.getNoteById(any()) }
        noteEditorViewModel.saveHeader("newHeader")
        noteEditorViewModel.saveBody("newBody")
        // Update.
        noteEditorViewModel.updateNote().joinWithTimeout()
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
        noteEditorViewModel.deleteNote().joinWithTimeout()
        coVerify(exactly = 1) { notesRoomRepository.deleteNoteById(noteMock.id) }
    }

    /**
     * Update note deleting blank.
     */
    @Test
    fun updateNote_deletingBlank() = runBlockingUnit {
        coEvery { notesRoomRepository.getNoteById(any()) } returns null
        noteEditorViewModel.init("0", null, null).joinWithTimeout()
        coVerify(exactly = 1) { notesRoomRepository.getNoteById(any()) }
        // Update.
        noteEditorViewModel.updateNote().joinWithTimeout()
        coVerify(exactly = 1) { notesRoomRepository.deleteNoteById("0") }
    }
}