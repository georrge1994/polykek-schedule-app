package argument.twins.com.polykekschedule.room.savedItems

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

private const val GET_SELECTED_ITEM = "SELECT * from SAVED_ITEMS WHERE isSelected = 1"

/**
 * Stores groups and professors - there are saved(selected) items. The selected item is used for requests and etc.
 */
@Dao
interface SavedItemsDao {
    @Query("SELECT * from SAVED_ITEMS ORDER BY name ASC")
    fun getItems(): LiveData<List<SavedItemEntity>>

    @Query(GET_SELECTED_ITEM)
    fun getSelectedItemFlow(): Flow<SavedItemEntity?>

    @Query(GET_SELECTED_ITEM)
    fun getSelectedItems(): SavedItemEntity?

    @Query(GET_SELECTED_ITEM)
    fun getSelectedItemLive(): LiveData<SavedItemEntity?>

    @Transaction
    suspend fun saveNewItem(item: SavedItemEntity) {
        unSelectAll()
        insert(item)
    }

    @Transaction
    suspend fun selectItem(item: SavedItemEntity) {
        unSelectAll()
        selectItem(item.id)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SavedItemEntity)

    @Query("UPDATE SAVED_ITEMS SET isSelected = 0")
    suspend fun unSelectAll()

    @Query("UPDATE SAVED_ITEMS SET isSelected = 1 WHERE id = :itemId")
    suspend fun selectItem(itemId: Int)

    @Query("DELETE FROM SAVED_ITEMS")
    suspend fun removeAll()

    @Delete
    suspend fun delete(item: SavedItemEntity)
}