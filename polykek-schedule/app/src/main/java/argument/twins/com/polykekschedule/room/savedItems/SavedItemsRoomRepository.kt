package argument.twins.com.polykekschedule.room.savedItems

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.*
import argument.twins.com.polykekschedule.dagger.core.DATABASE_DISPATCHER
import argument.twins.com.polykekschedule.room.savedItems.*
import com.android.common.models.savedItems.SavedItem
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.shared.code.utils.general.SharedPreferenceUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

private const val ITEM_IS_SELECTED = "GROUP_IS_SELECTED"

/**
 * Stores selected groups and professors. These objects are used for data fetching and UI-viewing.
 *
 * @property savedItemsDao [SavedItemsDao]
 * @property sharedPreferenceUtils Shared preference utils provides work wit shared preference memory
 * @property databaseDispatcher Single thread dispatcher to synchronize all requests to room to avoid concurrency exception
 * @constructor Create [SavedItemsRoomRepository]
 */
@Singleton
class SavedItemsRoomRepository @Inject constructor(
    private val savedItemsDao: SavedItemsDao,
    private val sharedPreferenceUtils: SharedPreferenceUtils,
    @Named(DATABASE_DISPATCHER) private val databaseDispatcher: CoroutineDispatcher
) : ISavedItemsRoomRepository {
    override val isItemSelected: Boolean
        get() = sharedPreferenceUtils.contains(ITEM_IS_SELECTED)

    override val savedItems: LiveData<List<SavedItem>> = savedItemsDao.getItems().map { list ->
        list.map { entity ->
            entity.toSavedItem()
        }
    }

    override val selectedItemLive2: LiveData<SavedItem?> = savedItemsDao.getSelectedItemLive().map { entity ->
        entity?.toSavedItem()
    }

    override fun getSelectedItemFlow(): Flow<SavedItem?> = savedItemsDao.getSelectedItemFlow().map { entity ->
        entity?.toSavedItem()
    }

    override suspend fun saveItemAndSelectIt(item: SavedItem) = withContext(databaseDispatcher) {
        savedItemsDao.saveNewItem(item.toEntity())
        sharedPreferenceUtils.add(ITEM_IS_SELECTED, true)
    }

    override suspend fun selectItem(item: SavedItem) = withContext(databaseDispatcher) {
        savedItemsDao.selectItem(item.toEntity())
    }

    override suspend fun insert(item: SavedItem) = withContext(databaseDispatcher) {
        savedItemsDao.insert(item.toEntity())
    }

    override suspend fun delete(item: SavedItem) = withContext(databaseDispatcher) {
        savedItemsDao.delete(item.toEntity())
        if (savedItemsDao.getSelectedItems() == null) {
            sharedPreferenceUtils.remove(ITEM_IS_SELECTED)
        }
    }

    /**
     * To saved item.
     *
     * @receiver [SavedItemEntity]
     * @return [SavedItem]
     */
    private fun SavedItemEntity.toSavedItem() = SavedItem(
        id = id,
        name = name,
        type = type,
        isSelected = isSelected
    )

    /**
     * To entity.
     *
     * @receiver [SavedItem]
     * @return [SavedItemEntity]
     */
    private fun SavedItem.toEntity() = SavedItemEntity(
        id = id,
        name = name,
        type = type,
        isSelected = isSelected
    )
}