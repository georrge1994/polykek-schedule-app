package com.android.feature.main.screen.saved.viewModels

import androidx.lifecycle.MutableLiveData
import com.android.common.models.savedItems.SavedItem
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.feature.main.screen.R
import com.android.feature.main.screen.saved.adapters.control.ControlItem
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.getOrAwaitValue
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * Saved items view model test for [SavedItemsViewModel].
 *
 * @constructor Create empty constructor for saved items view model test
 */
class SavedItemsViewModelTest : BaseViewModelUnitTest() {
    private val savedItem = SavedItem(id = 1, name = "1083/1", isSelected = true)
    private val savedItemsLiveData = MutableLiveData<List<SavedItem>>()
    private val savedItemsRoomRepository: ISavedItemsRoomRepository = mockk {
        coEvery { savedItems } returns savedItemsLiveData
        coEvery { selectItem(any()) } returns Unit
        coEvery { delete(any()) } returns Unit
    }
    private val savedItemsViewModel = SavedItemsViewModel(savedItemsRoomRepository)

    /**
     * Complex test.
     */
    @Test
    fun complexTest() = runBlockingUnit {
        savedItemsLiveData.postValue(listOf(savedItem))
        assertFalse(savedItemsViewModel.isEmpty.getOrAwaitValue())
        savedItemsViewModel.items.getOrAwaitValue().apply {
            assertEquals(4, size)
            assertEquals(savedItem, this[0])
            assertEquals(
                ControlItem(iconId = R.drawable.ic_baseline_person_add_24, textId = R.string.bottom_sheet_fragment_add_professor),
                this[1]
            )
            assertEquals(
                ControlItem(iconId = R.drawable.ic_baseline_group_add_24, textId = R.string.bottom_sheet_fragment_add_group),
                this[2]
            )
            assertEquals(
                ControlItem(iconId = R.drawable.ic_baseline_report_problem_24, textId = R.string.bottom_sheet_fragment_report_schedule_error),
                this[3]
            )
        }
        assertEquals("1083/1", savedItemsViewModel.getSelectedItem())
    }

    /**
     * Select item.
     */
    @Test
    fun selectItem() = runBlockingUnit {
        savedItemsViewModel.selectItem(savedItem).joinWithTimeout()
        coVerify(exactly = 1) { savedItemsRoomRepository.selectItem(savedItem) }
    }

    /**
     * Delete.
     */
    @Test
    fun delete() = runBlockingUnit {
        savedItemsViewModel.delete(savedItem).joinWithTimeout()
        coVerify(exactly = 1) { savedItemsRoomRepository.delete(savedItem) }
    }
}