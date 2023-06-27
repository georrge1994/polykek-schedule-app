package com.android.feature.buildings.mvi

import com.android.common.models.map.Building
import com.android.core.ui.mvi.MviAction

/**
 * Building action.
 *
 * @constructor Create empty constructor for building action
 */
internal sealed class BuildingAction : MviAction {
    /**
     * Select building.
     *
     * @property building Selected building
     * @constructor Create [SelectBuilding]
     */
    internal data class SelectBuilding(val building: Building) : BuildingAction()
}
