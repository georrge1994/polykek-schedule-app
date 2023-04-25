package com.android.feature.main.screen.saved.viewModels

import androidx.lifecycle.map
import com.android.common.models.savedItems.SavedItem
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.feature.main.screen.R
import com.android.feature.main.screen.saved.adapters.control.ControlItem
import javax.inject.Inject

/**
 * Saved items view model.
 *
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [SavedItemsViewModel]
 */
internal class SavedItemsViewModel @Inject constructor(
    private val savedItemsRoomRepository: ISavedItemsRoomRepository
) : BaseSubscriptionViewModel() {
    private var selectedItem: SavedItem? = null
    val items = savedItemsRoomRepository.savedItems.map { savedItems ->
        selectedItem = savedItems.firstOrNull { it.isSelected }
        savedItems.toMutableList<Any>().apply {
            add(ControlItem(iconId = R.drawable.ic_baseline_person_add_24, textId = R.string.bottom_sheet_fragment_add_professor))
            add(ControlItem(iconId = R.drawable.ic_baseline_group_add_24, textId = R.string.bottom_sheet_fragment_add_group))
            add(ControlItem(iconId = R.drawable.ic_baseline_report_problem_24, textId = R.string.bottom_sheet_fragment_report_schedule_error))
        }
    }
    val isEmpty = savedItemsRoomRepository.savedItems.map { it.isEmpty() }

    /**
     * Get selected item.
     *
     * @return Name or null
     */
    internal fun getSelectedItem() = selectedItem?.name ?: ""

    /**
     * Select item.
     *
     * @param savedItem Item
     */
    internal fun selectItem(savedItem: SavedItem) = launchInBackground {
        savedItemsRoomRepository.selectItem(savedItem)
    }

    /**
     * Delete.
     *
     * @param savedItem Item
     */
    internal fun delete(savedItem: SavedItem) = launchInBackground {
        savedItemsRoomRepository.delete(savedItem)
    }
}