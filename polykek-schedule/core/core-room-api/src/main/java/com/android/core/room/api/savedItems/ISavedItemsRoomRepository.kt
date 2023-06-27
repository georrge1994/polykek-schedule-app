package com.android.core.room.api.savedItems

import com.android.common.models.savedItems.SavedItem
import kotlinx.coroutines.flow.Flow

/**
 * Contract for saved items room repository.
 */
interface ISavedItemsRoomRepository {
    /**
     * Is any item selected. User can not remove the last item.
     */
    val isItemSelected: Boolean

    /**
     * All saved items.
     */
    val savedItems: Flow<List<SavedItem>>

    /**
     * Get selected saved item like a flow.
     *
     * @return [Flow]
     */
    fun getSelectedItemFlow(): Flow<SavedItem?>

    /**
     * Save item and select it.
     *
     * @param item Item
     */
    suspend fun saveItemAndSelectIt(item: SavedItem)

    /**
     * Select item.
     *
     * @param item Item
     */
    suspend fun selectItem(item: SavedItem)

    /**
     * Insert.
     *
     * @param item Item
     */
    suspend fun insert(item: SavedItem)

    /**
     * Delete.
     *
     * @param item Item
     */
    suspend fun delete(item: SavedItem)
}