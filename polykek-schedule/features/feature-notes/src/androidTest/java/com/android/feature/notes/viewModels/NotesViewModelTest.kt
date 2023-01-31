package com.android.feature.notes.viewModels

import com.android.common.models.savedItems.SavedItem
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.notes.Note
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.feature.notes.models.NotesTabTypes
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.getOrAwaitValue
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.testFixtures.waitActiveSubscription
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Notes view model test for [NotesViewModel].
 *
 * @constructor Create empty constructor for notes view model test
 */
class NotesViewModelTest : BaseViewModelUnitTest() {
    private val savedItemMock = SavedItem(id = 1, name = "1083/1")
    private val saveItemFlow = MutableSharedFlow<SavedItem?>()
    private val notes = listOf(
        Note(id = "1", name = "note1", header = "title1", body = "body1"),
        Note(id = "_own_note_2", name = "note2", header = "title2", body = "body2")
    )
    private val notesRoomRepository: INotesRoomRepository = mockk {
        coEvery { getNotesWhichContains(any(), any()) } returns notes
        coEvery { deleteNotesByIds(any()) } returns Unit
    }
    private val savedItemsRoomRepository: ISavedItemsRoomRepository = mockk {
        coEvery { getSelectedItemFlow() } returns saveItemFlow
    }
    private val notesViewModel = NotesViewModel(notesRoomRepository, savedItemsRoomRepository)

    /**
     * Complex test.
     */
    @Test
    fun complexTest() = runBlockingUnit {
        notesViewModel.asyncSubscribe().joinWithTimeout()
        saveItemFlow.waitActiveSubscription().emitAndWait(savedItemMock).joinWithTimeout()
        coVerify { notesRoomRepository.getNotesWhichContains(savedItemMock.id.toString(), any()) }
        assertEquals(
            notes.first(),
            notesViewModel.getNotesLiveData(NotesTabTypes.BY_LESSONS).getOrAwaitValue().first().note
        )
        notesViewModel.getNotesLiveData(NotesTabTypes.OWN_NOTES).getOrAwaitValue().apply {
            assertEquals(notes.first(), first().note)
            assertEquals(notes.last(), last().note)
        }
        notesViewModel.getIsEmptyNotesLivedata(NotesTabTypes.BY_LESSONS).getOrAwaitValue(false)
        notesViewModel.getIsEmptyNotesLivedata(NotesTabTypes.OWN_NOTES).getOrAwaitValue(false)
        notesViewModel.unSubscribe()
    }

    /**
     * Selecting and deselecting notes for removing.
     */
    @Test
    fun selectingAndDeselecting() = runBlockingUnit {
        notesViewModel.asyncSubscribe().joinWithTimeout()
        notesViewModel.clickByItem("1").joinWithTimeout()
        notesViewModel.selectionModeState.getOrAwaitValue(expectedResult = true)
        notesViewModel.clickByItem("1").joinWithTimeout()
        notesViewModel.selectionModeState.getOrAwaitValue(expectedResult = false)
        notesViewModel.unSubscribe()
    }

    /**
     * Delete selected notes.
     */
    @Test
    fun deleteSelectedNotes() = runBlockingUnit {
        notesViewModel.deleteSelectedNotes().joinWithTimeout()
        coVerify(exactly = 1) { notesRoomRepository.deleteNotesByIds(any()) }
        notesViewModel.selectionModeState.getOrAwaitValue(expectedResult = false)
        // Check update notes.
        notesViewModel.getIsEmptyNotesLivedata(NotesTabTypes.BY_LESSONS).getOrAwaitValue(false)
        notesViewModel.getIsEmptyNotesLivedata(NotesTabTypes.OWN_NOTES).getOrAwaitValue(false)
    }
}