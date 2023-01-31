package com.android.common.models.professors

import com.android.common.models.savedItems.ISavedItem
import com.android.common.models.savedItems.SavedItem
import com.android.common.models.savedItems.SavedItemTypes

/**
 * Professor.
 *
 * @property id Professor's unique id
 * @property fullName Full name
 * @property shortName Short name
 * @property chair Faculty or chair
 * @constructor Create [Professor]
 */
data class Professor(
    var id: Int,
    var fullName: String,
    var shortName: String,
    var chair: String
) : ISavedItem {
    override fun toSavedItem() = SavedItem(
        id = id,
        name = shortName,
        type = SavedItemTypes.PROFESSOR.name,
        isSelected = true
    )
}