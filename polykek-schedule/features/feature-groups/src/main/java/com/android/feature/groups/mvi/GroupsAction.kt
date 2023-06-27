package com.android.feature.groups.mvi

import com.android.core.ui.mvi.MviAction
import com.android.feature.groups.models.GroupType

/**
 * Groups action.
 *
 * @constructor Create empty constructor for group action
 */
internal sealed class GroupsAction : MviAction {
    /**
     * Select group.
     *
     * @property groupType tab mode
     * @constructor Create [SelectGroup]
     */
    internal data class SelectGroup(val groupType: GroupType) : GroupsAction()
}
