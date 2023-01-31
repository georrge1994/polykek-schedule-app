package com.android.feature.groups.models

import com.android.common.models.savedItems.ISavedItem
import com.android.common.models.savedItems.SavedItem
import com.android.common.models.savedItems.SavedItemTypes

/**
 * Group.
 *
 * @property id Group id
 * @property nameMultiLines Name in multi lines
 * @property nameOneLine Single line name
 * @property level There are the [level]-th year university students
 * @property groupType Type of group
 * @constructor Create [Group]
 */
internal data class Group(
    val id: Int,
    val nameMultiLines: String,
    val nameOneLine: String,
    val level: Int,
    val groupType: GroupType
) : ISavedItem {
    override fun toSavedItem() = SavedItem(
        id = id,
        name = nameOneLine,
        type = SavedItemTypes.GROUP.name,
        isSelected = true
    )
}