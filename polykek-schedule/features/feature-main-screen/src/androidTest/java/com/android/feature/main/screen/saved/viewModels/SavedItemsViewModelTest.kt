package com.android.feature.main.screen.saved.viewModels

import com.android.common.models.savedItems.SavedItem
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.feature.main.screen.R
import com.android.feature.main.screen.saved.adapters.control.ControlItem
import com.android.feature.main.screen.saved.mvi.SavedItemAction
import com.android.feature.main.screen.saved.mvi.SavedItemIntent
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
 * Saved items view model test for [SavedItemsViewModel].
 *
 * @constructor Create empty constructor for saved items view model test
 */
class SavedItemsViewModelTest : BaseViewModelUnitTest() {
    private val savedItem = SavedItem(id = 1, name = "1083/1", isSelected = true)
    private val savedItemsFlow = MutableSharedFlow<List<SavedItem>>()
    private val savedItemsRoomRepository: ISavedItemsRoomRepository = mockk {
        coEvery { savedItems } returns savedItemsFlow
        coEvery { selectItem(any()) } returns Unit
        coEvery { delete(any()) } returns Unit
    }
    private val savedItemsViewModel = SavedItemsViewModel(savedItemsRoomRepository)

    /**
     * Complex test.
     */
    @Test
    fun complexTest() = runBlockingUnit {
        savedItemsViewModel.asyncSubscribe().joinWithTimeout()
        savedItemsFlow.waitActiveSubscription().emitAndWait(listOf(savedItem)).joinWithTimeout()
        savedItemsViewModel.state.getOrAwaitValue().apply {
            val items = this.menuItems
            assertEquals(4, items.size)
            assertEquals(savedItem, items[0])
            assertEquals(
                ControlItem(
                    iconId = R.drawable.ic_baseline_person_add_24,
                    textId = R.string.bottom_sheet_fragment_add_professor
                ),
                items[1]
            )
            assertEquals(
                ControlItem(
                    iconId = R.drawable.ic_baseline_group_add_24,
                    textId = R.string.bottom_sheet_fragment_add_group
                ),
                items[2]
            )
            assertEquals(
                ControlItem(
                    iconId = R.drawable.ic_baseline_report_24,
                    textId = R.string.bottom_sheet_fragment_report_schedule_error
                ),
                items[3]
            )
        }
    }

    /**
     * Select item.
     */
    @Test
    fun selectItem() = runBlockingUnit {
        savedItemsViewModel.dispatchIntentAsync(SavedItemIntent.SelectItem(savedItem)).joinWithTimeout()
        coVerify(exactly = 1) { savedItemsRoomRepository.selectItem(savedItem) }
    }

    /**
     * Delete.
     */
    @Test
    fun delete() = runBlockingUnit {
        savedItemsViewModel.dispatchIntentAsync(SavedItemIntent.RemoveItem(savedItem)).joinWithTimeout()
        coVerify(exactly = 1) { savedItemsRoomRepository.delete(savedItem) }
    }

    /**
     * Open email chooser.
     */
    @Test
    fun openEmailChooser() = runBlockingUnit {
        savedItemsViewModel.asyncSubscribe().joinWithTimeout()
        savedItemsFlow.waitActiveSubscription().emitAndWait(listOf(savedItem)).joinWithTimeout()
        val actionJob = savedItemsViewModel.action.subscribeAndCompareFirstValue(
            SavedItemAction.OpenEmailChooser(savedItem.name)
        )
        savedItemsViewModel.dispatchIntentAsync(SavedItemIntent.OpenEmailChooser).joinWithTimeout()
        actionJob.joinWithTimeout()
    }

    /**
     * Open professors.
     */
    @Test
    fun openProfessors() = runBlockingUnit {
        val actionJob = savedItemsViewModel.action.subscribeAndCompareFirstValue(SavedItemAction.OpenProfessors)
        savedItemsViewModel.dispatchIntentAsync(SavedItemIntent.OpenProfessors).joinWithTimeout()
        actionJob.joinWithTimeout()
    }

    /**
     * Open schools.
     */
    @Test
    fun openSchools() = runBlockingUnit {
        val actionJob = savedItemsViewModel.action.subscribeAndCompareFirstValue(SavedItemAction.OpenSchools)
        savedItemsViewModel.dispatchIntentAsync(SavedItemIntent.OpenSchools).joinWithTimeout()
        actionJob.joinWithTimeout()
    }
}