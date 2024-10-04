package com.android.feature.buildings.mvi

import com.android.common.models.map.Building
import com.android.core.ui.mvi.SearchIntent

/**
 * Building intent.
 *
 * @constructor Create empty constructor for building intent
 */
internal sealed class BuildingIntent : SearchIntent {
    /**
     * Load content.
     */
    internal data object LoadContent : BuildingIntent()

    /**
     * Key word changed.
     *
     * @property keyWord Key word
     * @constructor Create [KeyWordChanged]
     */
    internal data class KeyWordChanged(val keyWord: String?) : BuildingIntent()

    /**
     * Building selected.
     *
     * @property building Selected building
     * @constructor Create [BuildingSelected]
     */
    internal data class BuildingSelected(val building: Building) : BuildingIntent()
}