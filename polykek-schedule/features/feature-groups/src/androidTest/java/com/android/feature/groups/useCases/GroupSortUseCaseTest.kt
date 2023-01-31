package com.android.feature.groups.useCases

import com.android.feature.groups.models.Group
import com.android.feature.groups.models.GroupType
import com.android.test.support.androidTest.BaseAndroidUnitTest
import com.android.test.support.testFixtures.TEST_STRING
import com.android.test.support.testFixtures.runBlockingUnit
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Group sort use case test for [GroupSortUseCase].
 *
 * @constructor Create empty constructor for group sort use case test
 */
class GroupSortUseCaseTest : BaseAndroidUnitTest() {
    private val groupSortUseCase = GroupSortUseCase(application)

    /**
     * Get sorted groups by tabs.
     */
    @Test
    fun getSortedGroupsByTabs() = runBlockingUnit {
        val groups = ArrayList<Group>()
        repeat(10) {
            groups.addGroup(index = it, name = TEST_STRING, groupType = GroupType.BACHELOR)
            groups.addGroup(index = it, name = TEST_STRING, groupType = GroupType.MASTER)
            groups.addGroup(index = it, name = TEST_STRING, groupType = GroupType.OTHER)
        }
        groupSortUseCase.getSortedGroupsByTabs(groups, TEST_STRING).apply {
            assertEquals(15, this[GroupType.BACHELOR]?.size)
            assertEquals(15, this[GroupType.MASTER]?.size)
            assertEquals(15, this[GroupType.OTHER]?.size)
        }
    }

    /**
     * Fetch the group, which contains "5" in the name.
     */
    @Test
    fun keyWordTest() = runBlockingUnit {
        val groups = ArrayList<Group>()
        repeat(8) { groups.addGroup(index = it, name = TEST_STRING + it, groupType = GroupType.BACHELOR) }
        groupSortUseCase.getSortedGroupsByTabs(groups, "5").apply {
            assertEquals(2, this[GroupType.BACHELOR]?.size)
            assertEquals(TEST_STRING, this[GroupType.BACHELOR]!![0])
            assertEquals(TEST_STRING + 5, (this[GroupType.BACHELOR]!![1] as Group).nameOneLine)
        }
    }

    /**
     * Check.
     */
    @Test
    fun check() = runBlockingUnit {
        val groups = ArrayList<Group>()
        repeat(10) { groups.addGroup(index = it, name = TEST_STRING, groupType = GroupType.MASTER) }
        groupSortUseCase.getSortedGroupsByTabs(groups, "").apply {
            assertEquals(TEST_STRING, this[GroupType.MASTER]!![0])
            assertEquals(TEST_STRING, this[GroupType.MASTER]!![3])
            assertEquals(TEST_STRING, this[GroupType.MASTER]!![6])
            assertEquals(TEST_STRING, this[GroupType.MASTER]!![9])
        }
    }

    /**
     * Add group.
     *
     * @receiver List of groups
     * @param index Index
     * @param name Name
     * @param groupType Group type
     */
    private fun ArrayList<Group>.addGroup(
        index: Int,
        name: String,
        groupType: GroupType
    ) = Group(
        id = index,
        nameMultiLines = name,
        nameOneLine = name,
        level = index / 2 + 1,
        groupType = groupType
    ).let {
        add(it)
    }
}