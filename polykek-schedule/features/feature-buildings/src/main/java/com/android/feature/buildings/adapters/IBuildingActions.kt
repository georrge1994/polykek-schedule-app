package com.android.feature.buildings.adapters

import com.android.common.models.map.Building

/**
 * Building actions.
 */
internal interface IBuildingActions {
    /**
     * On click.
     *
     * @param building Building
     */
    fun onClick(building: Building)
}