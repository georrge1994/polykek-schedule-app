package com.android.feature.notes.viewModels

import com.android.common.models.savedItems.SavedItem
import com.android.core.room.api.notes.Note
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.feature.notes.adapters.recycler.NoteItem
import com.android.feature.notes.models.NotesTabType
import com.android.feature.notes.mvi.NotesAction
import com.android.feature.notes.mvi.NotesIntent
import com.android.feature.notes.mvi.NotesState
import com.android.feature.notes.useCases.NotesUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
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
    private val notesMock = mapOf(
        NotesTabType.BY_LESSONS to listOf(NoteItem(Note(id = "1", name = "note1", header = "title1", body = "body1"))),
        NotesTabType.OWN_NOTES to listOf(NoteItem(Note(id = "_own_note_2", name = "note2", header = "title2", body = "body2"))),
    )
    private val notesUseCase: NotesUseCase = mockk {
        coEvery { getNotes(any(), any()) } returns notesMock
        coEvery { deleteNotesByIds(any()) } returns Unit
    }
    private val savedItemsRoomRepository: ISavedItemsRoomRepository = mockk {
        coEvery { getSelectedItemFlow() } returns saveItemFlow
    }
    private val notesViewModel = NotesViewModel(notesUseCase, savedItemsRoomRepository)

    /**
     * Complex test.
     */
    @Test
    fun complexTest() = runBlockingUnit {
        notesViewModel.asyncSubscribe().joinWithTimeout()
        saveItemFlow.waitActiveSubscription().emitAndWait(savedItemMock).joinWithTimeout()
        coVerify { notesUseCase.getNotes(savedItemMock.id, any()) }
        notesViewModel.state.value.checkState(
            isSelectionModeEnabled = false,
            tabPosition = 0,
            notes = notesMock,
            keyWord = null
        )
        notesViewModel.unSubscribe()
    }

    /**
     * Change key word.
     */
    @Test
    fun changeKeyWord() = runBlockingUnit {
        notesViewModel.state.value.checkState(
            isSelectionModeEnabled = false,
            tabPosition = 0,
            notes = emptyMap(),
            keyWord = null
        )
        notesViewModel.dispatchIntentAsync(NotesIntent.KeyWordChanged("abc")).joinWithTimeout()
        notesViewModel.state.value.checkState(
            isSelectionModeEnabled = false,
            tabPosition = 0,
            notes = notesMock,
            keyWord = "abc"
        )
    }

    /**
     * Selecting and deselecting notes for removing.
     */
    @Test
    fun selectingAndDeselecting() = runBlockingUnit {
        notesViewModel.state.value.checkState(
            isSelectionModeEnabled = false,
            tabPosition = 0,
            notes = emptyMap(),
            keyWord = null
        )
        notesViewModel.dispatchIntentAsync(NotesIntent.KeyWordChanged(keyWord = "abc")).joinWithTimeout()
        notesViewModel.dispatchIntentAsync(NotesIntent.LongPressByNote("1L")).joinWithTimeout()
        notesViewModel.state.value.checkState(
            isSelectionModeEnabled = true,
            tabPosition = 0,
            notes = notesMock,
            keyWord = "abc"
        )
        notesViewModel.dispatchIntentAsync(NotesIntent.ClickByNote("1L", NotesTabType.BY_LESSONS))
            .joinWithTimeout()
        notesViewModel.state.value.checkState(
            isSelectionModeEnabled = false,
            tabPosition = 0,
            notes = notesMock,
            keyWord = "abc"
        )
    }

    /**
     * Delete selected notes.
     */
    @Test
    fun deleteSelectedNotes() = runBlockingUnit {
        notesViewModel.state.value.checkState(
            isSelectionModeEnabled = false,
            tabPosition = 0,
            notes = emptyMap(),
            keyWord = null
        )
        notesViewModel.dispatchIntentAsync(NotesIntent.KeyWordChanged(keyWord = "abc")).joinWithTimeout()
        notesViewModel.dispatchIntentAsync(NotesIntent.LongPressByNote("1L")).joinWithTimeout()
        notesViewModel.state.value.checkState(
            isSelectionModeEnabled = true,
            tabPosition = 0,
            notes = notesMock,
            keyWord = "abc"
        )
        val actionJob = notesViewModel.action.subscribeAndCompareFirstValue(NotesAction.UpdateToolbar)
        notesViewModel.dispatchIntentAsync(NotesIntent.DeleteSelectedNotes).joinWithTimeout()
        coVerify(exactly = 1) { notesUseCase.deleteNotesByIds(any()) }
        notesViewModel.state.value.checkState(
            isSelectionModeEnabled = false,
            tabPosition = 0,
            notes = notesMock,
            keyWord = "abc"
        )
        actionJob.joinWithTimeout()
    }

    /**
     * Open note editor new.
     */
    @Test
    fun openNoteEditorNew() = runBlockingUnit {
        val actionJob = notesViewModel.action.subscribeAndCompareFirstValue(NotesAction.UpdateToolbar)
        notesViewModel.dispatchIntentAsync(NotesIntent.OpenNoteEditorNew).joinWithTimeout()
        actionJob.joinWithTimeout()
    }

    /**
     * Change tab.
     */
    @Test
    fun changeTab() = runBlockingUnit {
        notesViewModel.dispatchIntentAsync(NotesIntent.ChangeTab(1)).joinWithTimeout()
        notesViewModel.state.value.checkState(
            isSelectionModeEnabled = false,
            tabPosition = 1,
            notes = emptyMap(),
            keyWord = null
        )
    }

    /**
     * Check state.
     *
     * @receiver [NotesState]
     * @param isSelectionModeEnabled Is selection mode enabled
     * @param tabPosition Tab position
     * @param notes Notes
     * @param keyWord Key word
     */
    private fun NotesState.checkState(
        isSelectionModeEnabled: Boolean,
        tabPosition: Int,
        notes: Map<NotesTabType, List<NoteItem>>,
        keyWord: String?
    ) {
        assertEquals(isSelectionModeEnabled, this.isSelectionModeEnabled)
        assertEquals(tabPosition, this.tabPosition)
        assertEquals(notes, this.notes)
        assertEquals(keyWord, this.keyWord)
    }
}