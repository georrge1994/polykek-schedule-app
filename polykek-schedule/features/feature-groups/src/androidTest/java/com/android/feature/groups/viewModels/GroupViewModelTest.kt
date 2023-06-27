package com.android.feature.groups.viewModels

import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.feature.groups.models.Group
import com.android.feature.groups.models.GroupType
import com.android.feature.groups.mvi.GroupsAction
import com.android.feature.groups.mvi.GroupsIntent
import com.android.feature.groups.mvi.GroupsState
import com.android.feature.groups.useCases.GroupUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.testFixtures.joinWithTimeout
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Group view model test for [GroupUseCase].
 *
 * @constructor Create empty constructor for group view model test
 */
class GroupViewModelTest : BaseViewModelUnitTest() {
    private val groupsMockk = mapOf(
        GroupType.BACHELOR to GroupType.BACHELOR.getGroups(),
        GroupType.MASTER to GroupType.MASTER.getGroups(),
        GroupType.OTHER to GroupType.OTHER.getGroups()
    )
    private val groupUseCase: GroupUseCase = mockk {
        coEvery { getGroupsByTypes(any(), any()) } returns groupsMockk
        coEvery { getGroupsByTypes(any()) } returns groupsMockk
    }
    private val savedItemsRoomRepository: ISavedItemsRoomRepository = mockk()
    private val groupViewModel = GroupViewModel(
        groupUseCase = groupUseCase,
        savedItemsRoomRepository = savedItemsRoomRepository
    )

    /**
     * Load content.
     */
    @Test
    fun loadContent() = runBlockingUnit {
        groupViewModel.state.collectPost {
            groupViewModel.dispatchIntentAsync(GroupsIntent.LoadContent(schoolId = "1")).joinWithTimeout()
            coVerify(exactly = 1) { groupUseCase.getGroupsByTypes(any(), any()) }
        }.apply {
            assertEquals(3, this.size)
            this[0].checkState(items = emptyMap(), isLoading = false, tabPosition = 0, keyWord = null)  // Default.
            this[1].checkState(items = emptyMap(), isLoading = true, tabPosition = 0, keyWord = null)   // Loading.
            this[2].checkState(items = groupsMockk, isLoading = false, tabPosition = 0, keyWord = null) // New data
        }
    }

    /**
     * Change key word.
     */
    @Test
    fun changeKeyWord() = runBlockingUnit {
        groupViewModel.state.collectPost {
            groupViewModel.dispatchIntentAsync(GroupsIntent.KeyWordChanged(keyWord = "abc")).joinWithTimeout()
            coVerify(exactly = 1) { groupUseCase.getGroupsByTypes(any()) }
        }.apply {
            assertEquals(2, this.size)
            this[0].checkState(items = emptyMap(), isLoading = false, tabPosition = 0, keyWord = null)   // Default.
            this[1].checkState(items = groupsMockk, isLoading = false, tabPosition = 0, keyWord = "abc")  // Change key.
        }
    }

    /**
     * Select and save item.
     */
    @Test
    fun selectAndSaveItem() = runBlockingUnit {
        val group = GroupType.BACHELOR.getGroups().first()
        coEvery { savedItemsRoomRepository.saveItemAndSelectIt(any()) } returns Unit
        val actionJob = groupViewModel.action.subscribeAndCompareFirstValue(
            GroupsAction.SelectGroup(GroupType.BACHELOR))
        groupViewModel.dispatchIntentAsync(GroupsIntent.GroupSelected(group, GroupType.BACHELOR)).joinWithTimeout()
        coVerify(exactly = 1) { savedItemsRoomRepository.saveItemAndSelectIt(group.toSavedItem()) }
        actionJob.joinWithTimeout()
    }

    /**
     * Change tab.
     */
    @Test
    fun changeTab() = runBlockingUnit {
        groupViewModel.dispatchIntentAsync(GroupsIntent.ChangeTab(2)).joinWithTimeout()
        groupViewModel.state.value.checkState(items = emptyMap(), isLoading = false, tabPosition = 2, keyWord = null)
    }

    /**
     * Get groups.
     *
     * @receiver [GroupType]
     * @return Groups
     */
    private fun GroupType.getGroups() = listOf(
        Group(
            id = 0,
            nameMultiLines = TEST_STRING,
            nameOneLine = TEST_STRING,
            level = 0,
            groupType = this
        )
    )

    /**
     * Check state.
     *
     * @receiver [GroupsState]
     * @param items Items
     * @param isLoading Is loading
     * @param tabPosition Tab position
     * @param keyWord Key word
     */
    private fun GroupsState?.checkState(
        items: Map<GroupType, List<Any?>>,
        isLoading: Boolean,
        tabPosition: Int,
        keyWord: String?
    ) {
        this ?: throw Exception("State is null")
        assertEquals(items, this.items)
        assertEquals(isLoading, this.isLoading)
        assertEquals(tabPosition, this.tabPosition)
        assertEquals(keyWord, this.keyWord)
    }
}