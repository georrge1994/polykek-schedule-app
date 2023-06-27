package com.android.feature.buildings.mvi

import com.android.common.models.map.Building
import com.android.core.ui.mvi.SearchState

/**
 * Building state properties.
 */
internal interface BuildingStateProperties : SearchState {
    val buildings: List<Building>
    val isLoading: Boolean
}

/**
 * Building state.
 *
 * @constructor Create empty constructor for building state
 */
internal sealed class BuildingState : SearchState, BuildingStateProperties {
    /**
     * Default state.
     */
    internal object Default : BuildingState() {
        override val keyWord: String? = null
        override val buildings: List<Building> = emptyList()
        override val isLoading: Boolean = false
    }

    /**
     * Update.
     *
     * @property keyWord Key word
     * @property buildings List of buildings
     * @property isLoading Loading state
     * @constructor Create [Update]
     */
    internal data class Update(
        override val keyWord: String?,
        override val buildings: List<Building>,
        override val isLoading: Boolean
    ) : BuildingState()

    /**
     * Copy state.
     *
     * @param keyWord Key word
     * @param buildings Buildings
     * @param isLoading Is loading
     * @return [BuildingState]
     */
    internal fun copyState(
        keyWord: String? = this.keyWord,
        buildings: List<Building> = this.buildings,
        isLoading: Boolean = this.isLoading
    ): BuildingState = Update(keyWord, buildings, isLoading)
}