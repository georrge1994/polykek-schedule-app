package com.android.feature.groups.viewModels

import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.feature.groups.models.Group
import com.android.feature.groups.models.GroupType
import com.android.feature.groups.useCases.GroupUseCase
import com.android.test.support.androidTest.base.BaseViewModelUnitTest
import com.android.test.support.androidTest.utils.collectPost
import com.android.test.support.androidTest.utils.getOrAwaitValue
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
    private val groupUseCase: GroupUseCase = mockk {
        coEvery { getGroupsByTypes(any(), any()) } returns mapOf(
            GroupType.BACHELOR to GroupType.BACHELOR.getGroups(),
            GroupType.MASTER to GroupType.MASTER.getGroups(),
            GroupType.OTHER to GroupType.OTHER.getGroups()
        )
    }
    private val savedItemsRoomRepository: ISavedItemsRoomRepository = mockk()
    private val groupViewModel = GroupViewModel(
        groupUseCase = groupUseCase,
        savedItemsRoomRepository = savedItemsRoomRepository
    )

    /**
     * Select and save item.
     */
    @Test
    fun selectAndSaveItem() = runBlockingUnit {
        val group = GroupType.BACHELOR.getGroups().first()
        coEvery { savedItemsRoomRepository.saveItemAndSelectIt(any()) } returns Unit
        groupViewModel.selectAndSaveItem(group).joinWithTimeout()
        coVerify(exactly = 1) { savedItemsRoomRepository.saveItemAndSelectIt(group.toSavedItem()) }
    }

    /**
     * Get groups live data by group type.
     */
    @Test
    fun getGroupsLiveDataByGroupType() = runBlockingUnit {
        groupViewModel.isLoading.collectPost {
            groupViewModel.updateGroups(schoolId = "100").joinWithTimeout()
            groupViewModel.getGroupsLiveDataByGroupType(GroupType.BACHELOR).getOrAwaitValue(GroupType.BACHELOR.getGroups())
            groupViewModel.getGroupsLiveDataByGroupType(GroupType.MASTER).getOrAwaitValue(GroupType.MASTER.getGroups())
            groupViewModel.getGroupsLiveDataByGroupType(GroupType.OTHER).getOrAwaitValue(GroupType.OTHER.getGroups())
            coVerify(exactly = 1) { groupUseCase.getGroupsByTypes(any(), any()) }
        }.apply {
            assertEquals(2, this.size)
            assertEquals(true, first())
            assertEquals(false, last())
        }
    }

    /**
     * Key word was changed.
     */
    @Test
    fun keyWordWasChanged() = runBlockingUnit {
        groupViewModel.isLoading.collectPost {
            groupViewModel.updateKeyWordAsync(keyWord = "100").joinWithTimeout()
            groupViewModel.getGroupsLiveDataByGroupType(GroupType.BACHELOR).getOrAwaitValue(GroupType.BACHELOR.getGroups())
            groupViewModel.getGroupsLiveDataByGroupType(GroupType.MASTER).getOrAwaitValue(GroupType.MASTER.getGroups())
            groupViewModel.getGroupsLiveDataByGroupType(GroupType.OTHER).getOrAwaitValue(GroupType.OTHER.getGroups())
            coVerify(exactly = 1) { groupUseCase.getGroupsByTypes(any(), any()) }
        }.apply {
            assertEquals(2, this.size)
            assertEquals(true, first())
            assertEquals(false, last())
        }
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
}