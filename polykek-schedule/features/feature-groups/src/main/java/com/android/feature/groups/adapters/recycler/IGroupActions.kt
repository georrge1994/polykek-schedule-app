package com.android.feature.groups.adapters.recycler

import com.android.feature.groups.models.Group

/**
 * Group action interface.
 */
internal interface IGroupActions {
    /**
     * On click.
     *
     * @param group Group
     */
    fun onClick(group: Group)
}