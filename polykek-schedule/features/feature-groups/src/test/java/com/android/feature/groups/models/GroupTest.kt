package com.android.feature.groups.models

import com.android.common.models.savedItems.SavedItem
import com.android.test.support.unitTest.BaseUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Group test for converting [Group] to [SavedItem].
 *
 * @constructor Create empty constructor for group test
 */
class GroupTest : BaseUnitTest() {
    /**
     * Get saved item test.
     */
    @Test
    fun getSavedItemTest() {
        assertEquals(
            SavedItem(
                id = 1,
                name = "1083/1",
                type = "GROUP",
                isSelected = true
            ),
            Group(
                id = 1,
                nameMultiLines = "1083/1",
                nameOneLine = "1083/1",
                level = 3,
                groupType = GroupType.BACHELOR
            ).toSavedItem()
        )
    }
}