package argument.twins.com.polykekschedule.room.savedItems

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.common.models.savedItems.SavedItemTypes

internal const val SAVED_ITEMS = "saved_items"

internal const val SAVED_ITEMS_ID = "id"
internal const val SAVED_ITEMS_NAME = "name"
internal const val SAVED_ITEMS_TYPE = "type"
internal const val SAVED_ITEMS_IS_SELECTED = "isSelected"

/**
 * Saved item is group or professor.
 *
 * @property id  Saved item id
 * @property name Name
 * @property type Type
 * @property isSelected Is current item
 * @constructor Create [SavedItemEntity]
 */
@Entity(tableName = SAVED_ITEMS)
data class SavedItemEntity(
    @PrimaryKey
    @ColumnInfo(name = SAVED_ITEMS_ID) val id: Int,
    @ColumnInfo(name = SAVED_ITEMS_NAME) val name: String,
    @ColumnInfo(name = SAVED_ITEMS_TYPE) val type: String = SavedItemTypes.GROUP.name,
    @ColumnInfo(name = SAVED_ITEMS_IS_SELECTED) var isSelected: Boolean = false
)