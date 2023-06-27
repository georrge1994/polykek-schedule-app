package com.android.feature.groups.mvi

import com.android.core.ui.mvi.SearchIntent
import com.android.feature.groups.models.Group
import com.android.feature.groups.models.GroupType

/**
 * Groups intent.
 *
 * @constructor Create empty constructor for groups intent
 */
internal sealed class GroupsIntent : SearchIntent {
    /**
     * Load content.
     *
     * @property schoolId School id
     * @constructor Create [LoadContent]
     */
    internal data class LoadContent(val schoolId: String?) : GroupsIntent()

    /**
     * Key word changed.
     *
     * @property keyWord Key word
     * @constructor Create [KeyWordChanged]
     */
    internal data class KeyWordChanged(val keyWord: String?) : GroupsIntent()

    /**
     * Change tab.
     *
     * @property tabPosition Tab position
     * @constructor Create [ChangeTab]
     */
    internal data class ChangeTab(val tabPosition: Int) : GroupsIntent()

    /**
     * Group selected.
     *
     * @property group Group
     * @property tabType Tab type
     * @constructor Create [GroupSelected]
     */
    internal data class GroupSelected(val group: Group, val tabType: GroupType) : GroupsIntent()
}