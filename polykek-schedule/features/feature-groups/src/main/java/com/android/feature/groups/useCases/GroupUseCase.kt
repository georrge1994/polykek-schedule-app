package com.android.feature.groups.useCases

import com.android.core.retrofit.api.common.CatchResourceUseCase
import com.android.core.ui.dagger.BACKGROUND_MESSAGE_BUS
import com.android.feature.groups.api.GroupsApiRepository
import com.android.feature.groups.api.GroupsResponse
import com.android.feature.groups.models.Group
import com.android.feature.groups.models.GroupType
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Named

private const val BACHELOR_CODE = "0"
private const val MASTER_CODE = "1"

/**
 * Main functions of this class:
 * - Provides the caching of request;
 * - Data representation;
 *
 * @property groupsApiRepository Provides api for fetching groups
 * @property groupSortUseCase Provides sorting of groups by tabs and keyword
 * @constructor Create [GroupUseCase]
 *
 * @param backgroundMessageBus Common background message bus with snack-bar output
 */
internal class GroupUseCase @Inject constructor(
    private val groupsApiRepository: GroupsApiRepository,
    private val groupSortUseCase: GroupSortUseCase,
    @Named(BACKGROUND_MESSAGE_BUS) backgroundMessageBus: MutableSharedFlow<String>
) : CatchResourceUseCase(backgroundMessageBus) {
    private val nameAndLevelComparator = compareBy<Group>({ it.level }, { it.nameMultiLines })
    private var cachedGroups: List<Group>? = null
    private var cachedSchoolId: String? = null

    /**
     * Get groups by types.
     *
     * @param schoolId School id
     * @param keyWord Key word
     * @return Data for recycler views (Lists of groups by [GroupType] with level titles).
     */
    internal suspend fun getGroupsByTypes(
        schoolId: String?,
        keyWord: String
    ): Map<GroupType, List<Any>>? = if (schoolId == cachedSchoolId || schoolId == null) {
        groupSortUseCase.getSortedGroupsByTabs(cachedGroups, keyWord)
    } else {
        groupsApiRepository.getGroups(schoolId).catchRequestError {
            cachedSchoolId = schoolId
            cachedGroups = it.convertToCachedGroups()
            groupSortUseCase.getSortedGroupsByTabs(cachedGroups, keyWord)
        }
    }

    /**
     * Convert [GroupsResponse] to list of [Group].
     *
     * @receiver [GroupsResponse] or null
     * @return List of sorted groups
     */
    private fun GroupsResponse?.convertToCachedGroups(): List<Group>? =
        this?.groups?.map {
            Group(
                id = it.id,
                nameMultiLines = it.name.replace("/", "/\n"),
                nameOneLine = it.name,
                level = it.level,
                groupType = it.kind.toGroupType()
            )
        }?.sortedWith(nameAndLevelComparator)

    /**
     * To group type.
     *
     * @receiver Group type in string format
     * @return [GroupType]
     */
    private fun String.toGroupType() = when (this) {
        BACHELOR_CODE -> GroupType.BACHELOR
        MASTER_CODE -> GroupType.MASTER
        else -> GroupType.OTHER
    }
}