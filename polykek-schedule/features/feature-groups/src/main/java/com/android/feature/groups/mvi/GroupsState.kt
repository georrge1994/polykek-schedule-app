package com.android.feature.groups.mvi

import com.android.core.ui.mvi.SearchState
import com.android.feature.groups.models.GroupType

/**
 * Groups state properties.
 */
internal interface GroupsStateProperties {
    val items: Map<GroupType, List<Any>>?
    val isLoading: Boolean
    val tabPosition: Int
}

/**
 * Groups state.
 *
 * @constructor Create empty constructor for groups state
 */
internal sealed class GroupsState : SearchState, GroupsStateProperties {
    /**
     * Default.
     */
    internal data object Default : GroupsState() {
        override val keyWord: String? = null
        override val items: Map<GroupType, List<Any>> = emptyMap()
        override val isLoading: Boolean = false
        override val tabPosition: Int = 0
    }

    /**
     * Update.
     *
     * @property keyWord Key word
     * @property items Items
     * @property isLoading Is loading
     * @property tabPosition Tab position
     * @constructor Create [Update]
     */
    internal data class Update(
        override val keyWord: String?,
        override val items: Map<GroupType, List<Any>>?,
        override val isLoading: Boolean,
        override val tabPosition: Int
    ) : GroupsState()

    /**
     * Copy state.
     *
     * @param keyWord Key word
     * @param items Items
     * @param isLoading Is loading
     * @param tabPosition Tab position
     * @return [Update]
     */
    internal fun copyState(
        keyWord: String? = this.keyWord,
        items: Map<GroupType, List<Any>>? = this.items,
        isLoading: Boolean = this.isLoading,
        tabPosition: Int = this.tabPosition
    ): Update = Update(
        keyWord = keyWord,
        items = items,
        isLoading = isLoading,
        tabPosition = tabPosition
    )
}
