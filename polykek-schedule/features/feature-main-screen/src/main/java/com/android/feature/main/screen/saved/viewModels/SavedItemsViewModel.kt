package com.android.feature.main.screen.saved.viewModels

import com.android.common.models.savedItems.SavedItem
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.viewModels.BaseSubscriptionViewModel
import com.android.feature.main.screen.R
import com.android.feature.main.screen.saved.adapters.control.ControlItem
import com.android.feature.main.screen.saved.mvi.SavedItemAction
import com.android.feature.main.screen.saved.mvi.SavedItemIntent
import com.android.feature.main.screen.saved.mvi.SavedItemState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Saved items view model.
 *
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [SavedItemsViewModel]
 */
internal class SavedItemsViewModel @Inject constructor(
    private val savedItemsRoomRepository: ISavedItemsRoomRepository
) : BaseSubscriptionViewModel<SavedItemIntent, SavedItemState, SavedItemAction>(SavedItemState.Default) {
    override suspend fun subscribe() {
        super.subscribe()
        // Subscribe to selected items.
        savedItemsRoomRepository.savedItems.onEach { savedItems ->
            updateMenu(savedItems)
        }.cancelableLaunchInBackground()
    }

    override suspend fun dispatchIntent(intent: SavedItemIntent) {
        when (intent) {
            is SavedItemIntent.SelectItem -> selectItem(intent.savedItem)
            is SavedItemIntent.RemoveItem -> removeItem(intent.savedItem)
            SavedItemIntent.OpenEmailChooser -> openEmailChooser()
            SavedItemIntent.OpenProfessors -> SavedItemAction.OpenProfessors.emitAction()
            SavedItemIntent.OpenSchools -> SavedItemAction.OpenSchools.emitAction()
        }
    }

    /**
     * Update menu.
     *
     * @param savedItems Saved items
     */
    private suspend fun updateMenu(savedItems: List<SavedItem>) = withContext(Dispatchers.Default) {
        savedItems.toMutableList<Any>().apply {
            addControlItem(R.drawable.ic_baseline_person_add_24, R.string.bottom_sheet_fragment_add_professor)
            addControlItem(R.drawable.ic_baseline_group_add_24, R.string.bottom_sheet_fragment_add_group)
            addControlItem(R.drawable.ic_baseline_report_24, R.string.bottom_sheet_fragment_report_schedule_error)
        }.apply {
            currentState.copyState(menuItems = this).emitState()
        }
    }

    /**
     * Add control item.
     *
     * @receiver List of menu items and control-buttons
     * @param iconId Icon id
     * @param textId Text id
     */
    private fun MutableList<Any>.addControlItem(iconId: Int, textId: Int) = add(ControlItem(iconId, textId))

    /**
     * Select item.
     *
     * @param savedItem Item
     */
    private suspend fun selectItem(savedItem: SavedItem) = withContext(Dispatchers.IO) {
        savedItemsRoomRepository.selectItem(savedItem)
    }

    /**
     * Delete.
     *
     * @param savedItem Item
     */
    private suspend fun removeItem(savedItem: SavedItem) = withContext(Dispatchers.IO) {
        savedItemsRoomRepository.delete(savedItem)
    }

    /**
     * Open email chooser.
     */
    private suspend fun openEmailChooser() = withContext(Dispatchers.Default) {
        val selectedItem = currentState.menuItems.first { it is SavedItem && it.isSelected } as SavedItem
        SavedItemAction.OpenEmailChooser(selectedItem.name).emitAction()
    }
}