package com.android.common.models.savedItems

/**
 * Saved item is group or professor.
 *
 * @property id  Saved item id
 * @property name Name
 * @property type Type
 * @property isSelected Is current item
 * @constructor Create [SavedItem]
 */
data class SavedItem(
    val id: Int,
    val name: String,
    val type: String = SavedItemTypes.GROUP.name,
    var isSelected: Boolean = false
) {
    val isGroup: Boolean
        get() = type == SavedItemTypes.GROUP.name
}