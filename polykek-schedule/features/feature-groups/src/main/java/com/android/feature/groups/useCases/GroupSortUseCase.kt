package com.android.feature.groups.useCases

import android.app.Application
import com.android.feature.groups.R
import com.android.feature.groups.models.Group
import com.android.feature.groups.models.GroupType
import com.android.shared.code.utils.general.getZeroIfNull
import com.android.shared.code.utils.markers.IUseCase
import javax.inject.Inject

/**
 * Provides sorting of groups by tabs and keyword.
 *
 * @property application Application object to get context
 * @constructor Create [GroupSortUseCase]
 */
internal class GroupSortUseCase @Inject constructor(private val application: Application) : IUseCase {
    /**
     * Get sorted groups by tabs and keyword.
     *
     * @param groups Groups
     * @param keyWord Key word
     * @return Three tabs of sorted groups
     */
    internal fun getSortedGroupsByTabs(
        groups: List<Group>?,
        keyWord: String
    ): Map<GroupType, List<Any>> = mutableMapOf<GroupType, ArrayList<Any>>(
        GroupType.BACHELOR to ArrayList(),
        GroupType.MASTER to ArrayList(),
        GroupType.OTHER to ArrayList()
    ).apply {
        groups ?: return@apply
        val levelCounters = mutableMapOf(
            GroupType.BACHELOR to 0,
            GroupType.MASTER to 0,
            GroupType.OTHER to 0
        )
        groups.forEach { group ->
            if (group.nameMultiLines.contains(keyWord, ignoreCase = true)) {
                if (levelCounters[group.groupType].getZeroIfNull() < group.level) {
                    // Add header.
                    levelCounters[group.groupType] = group.level
                    this[group.groupType]?.add(application.getString(R.string.group_fragment_curse, group.level))
                }
                this[group.groupType]?.add(group)
            }
        }
    }
}