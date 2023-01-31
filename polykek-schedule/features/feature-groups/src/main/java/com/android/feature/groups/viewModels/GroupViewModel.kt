package com.android.feature.groups.viewModels

import androidx.lifecycle.MutableLiveData
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.viewModels.SearchViewModel
import com.android.feature.groups.models.Group
import com.android.feature.groups.models.GroupType
import com.android.feature.groups.useCases.GroupUseCase
import javax.inject.Inject

/**
 * Provides logic for group screen.
 *
 * @property groupUseCase Provides the caching of request and data representation
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [GroupViewModel]
 */
internal class GroupViewModel @Inject constructor(
    private val groupUseCase: GroupUseCase,
    private val savedItemsRoomRepository: ISavedItemsRoomRepository
) : SearchViewModel() {
    private val bachelorGroups = MutableLiveData<List<Any>?>()
    private val masterGroups = MutableLiveData<List<Any>?>()
    private val otherGroups = MutableLiveData<List<Any>?>()

    override suspend fun keyWordWasChanged(keyWord: String?) {
        super.keyWordWasChanged(keyWord)
        updateGroups()
    }

    /**
     * Get groups live data by group type.
     *
     * @param tabType Tab type
     * @return Specified [MutableLiveData]
     */
    internal fun getGroupsLiveDataByGroupType(tabType: GroupType?) = when (tabType) {
        GroupType.BACHELOR -> bachelorGroups
        GroupType.MASTER -> masterGroups
        else -> otherGroups
    }

    /**
     * Update groups.
     *
     * @param schoolId School id
     */
    internal fun updateGroups(schoolId: String? = null) = executeWithLoadingAnimation {
        groupUseCase.getGroupsByTypes(schoolId, keyWord)?.let {
            bachelorGroups.postValue(it[GroupType.BACHELOR])
            masterGroups.postValue(it[GroupType.MASTER])
            otherGroups.postValue(it[GroupType.OTHER])
        }
    }

    /**
     * Save item.
     *
     * @param group Group
     */
    internal fun selectAndSaveItem(group: Group) = launchInBackground {
        savedItemsRoomRepository.saveItemAndSelectIt(group.toSavedItem())
    }
}