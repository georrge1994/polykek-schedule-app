package com.android.professors.base.models

import com.android.common.models.professors.Professor
import com.android.common.models.savedItems.SavedItem
import com.android.common.models.savedItems.SavedItemTypes
import com.android.test.support.unitTest.BaseUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Professor test for [Professor].
 *
 * @constructor Create empty constructor for professor test
 */
class ProfessorTest : BaseUnitTest() {
    /**
     * Convert professor to saved item.
     */
    @Test
    fun convertProfessorToSavedItem() {
        assertEquals(
            SavedItem(
                id = 1,
                name = "Ivanov I. M.",
                type = SavedItemTypes.PROFESSOR.name,
                isSelected = true
            ),
            Professor(
                id = 1,
                fullName = "Ivanov Ivan Mikhailovich",
                shortName = "Ivanov I. M.",
                chair = "professor"
            ).toSavedItem()
        )
    }
}