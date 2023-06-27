package com.android.feature.groups.viewModels

import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.viewModels.SearchViewModel
import com.android.feature.groups.models.Group
import com.android.feature.groups.models.GroupType
import com.android.feature.groups.mvi.GroupsAction
import com.android.feature.groups.mvi.GroupsIntent
import com.android.feature.groups.mvi.GroupsState
import com.android.feature.groups.useCases.GroupUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
) : SearchViewModel<GroupsIntent, GroupsState, GroupsAction>(GroupsState.Default) {
    override suspend fun dispatchIntent(intent: GroupsIntent) {
        when (intent) {
            is GroupsIntent.LoadContent -> loadGroups(intent.schoolId, keyWordFromLastState)
            is GroupsIntent.KeyWordChanged -> updateGroups(intent.keyWord)
            is GroupsIntent.GroupSelected -> selectAndSaveItem(intent.group, intent.tabType)
            is GroupsIntent.ChangeTab -> changeTab(intent.tabPosition)
        }
    }

    /**
     * Load groups.
     *
     * @param schoolId School id
     * @param keyWord Key word
     */
    private suspend fun loadGroups(schoolId: String?, keyWord: String?) = withContext(Dispatchers.IO) {
        currentState.copyState(keyWord = keyWord, isLoading = true).emitState()
        currentState.copyState(
            items = groupUseCase.getGroupsByTypes(schoolId, keyWord ?: ""),
            isLoading = false
        ).emitState()
    }

    /**
     * Update groups.
     *
     * @param keyWord Key word
     */
    private suspend fun updateGroups(keyWord: String?) = withContext(Dispatchers.Default) {
        currentState.copyState(
            keyWord = keyWord,
            items = groupUseCase.getGroupsByTypes(keyWord ?: "")
        ).emitState()
    }

    /**
     * Save item.
     *
     * @param group Group
     * @param tabType Tab type
     */
    private suspend fun selectAndSaveItem(group: Group, tabType: GroupType) = withContext(Dispatchers.IO) {
        savedItemsRoomRepository.saveItemAndSelectIt(group.toSavedItem())
        GroupsAction.SelectGroup(tabType).emitAction()
    }

    /**
     * Change tab.
     *
     * @param tabPosition Tab position
     * @return [GroupsState]
     */
    private suspend fun changeTab(tabPosition: Int) = withContext(Dispatchers.Default) {
        currentState.copyState(tabPosition = tabPosition).emitState()
    }
}