package argument.twins.com.polykekschedule.room.savedItems

import com.android.common.models.savedItems.SavedItem
import com.android.test.support.androidTest.BaseAndroidUnitTest
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Saved item test for [SavedItem].
 *
 * @constructor Create empty constructor for saved item test
 */
class SavedItemTest : BaseAndroidUnitTest() {
    /**
     * Simple test for is group.
     */
    @Test
    fun isGroup() {
        assertTrue(
            SavedItem(
                id = 1,
                name = "name",
                type = "GROUP",
                isSelected = true
            ).isGroup
        )
    }
}