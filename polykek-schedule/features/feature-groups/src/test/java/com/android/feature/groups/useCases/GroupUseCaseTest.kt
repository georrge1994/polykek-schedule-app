package com.android.feature.groups.useCases

import com.android.common.models.groups.GroupResponse
import com.android.feature.groups.api.GroupsApiRepository
import com.android.feature.groups.api.GroupsResponse
import com.android.feature.groups.models.Group
import com.android.feature.groups.models.GroupType
import com.android.common.models.api.Resource
import com.android.test.support.testFixtures.runBlockingUnit
import com.android.test.support.unitTest.base.BaseUnitTestForSubscriptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Group use case test for [GroupUseCase].
 *
 * @constructor Create empty constructor for group use case test
 */
class GroupUseCaseTest : BaseUnitTestForSubscriptions() {
    private val groupsApiRepository: GroupsApiRepository = mockk()
    private val groupSortUseCase: GroupSortUseCase = mockk()
    private val backgroundMessageBus = MutableSharedFlow<String>()
    private val groupUseCase = GroupUseCase(
        groupsApiRepository = groupsApiRepository,
        groupSortUseCase = groupSortUseCase,
        backgroundMessageBus = backgroundMessageBus
    )

    /**
     * Get groups by types.
     */
    @Test
    fun getGroupsByTypes() = runBlockingUnit {
        val groupResponse = getGroupsResponse()
        val groups = getGroups()
        coEvery { groupsApiRepository.getGroups(any()) } returns groupResponse
        coEvery { groupSortUseCase.getSortedGroupsByTabs(any(), "374") } returns groups
        val result = groupUseCase.getGroupsByTypes("100", "374")
        assertEquals(
            groups[GroupType.BACHELOR]?.firstOrNull(),
            result?.get(GroupType.BACHELOR)?.firstOrNull()
        )
        coVerify(exactly = 1) {
            groupsApiRepository.getGroups("100")
            groupSortUseCase.getSortedGroupsByTabs(any(), "374")
        }
    }

    /**
     * Get groups response.
     *
     * @return [GroupsResponse] like a success result
     */
    private fun getGroupsResponse() = Resource.Success(
        data = GroupsResponse(
            groups = listOf(
                GroupResponse(
                    id = 35585,
                    name = "3743804/20101",
                    level = 1,
                    type = "common",
                    kind = "1",
                    specialization = "38.04.04 Государственное и муниципальное управление"
                ),
                GroupResponse(
                    id = 35585,
                    name = "1083/1",
                    level = 1,
                    type = "common",
                    kind = "1",
                    specialization = "38.04.04 Государственное и муниципальное управление"
                )
            )
        )
    )

    /**
     * Get groups.
     *
     * @return Groups
     */
    private fun getGroups() = mapOf(
        GroupType.BACHELOR to listOf(
            Group(
                id = 35585,
                nameMultiLines = "3743804/20101",
                nameOneLine = "3743804/20101",
                level = 1,
                groupType = GroupType.BACHELOR
            )
        )
    )
}