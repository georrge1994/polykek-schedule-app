package argument.twins.com.polykekschedule.room.dataGenerator

import com.android.common.models.savedItems.SavedItem
import com.android.test.support.testFixtures.TestDataGenerator

/**
 * Saved item data generator for [SavedItem].
 *
 * @constructor Create empty constructor for saved item data generator
 */
class SavedItemDataGenerator : TestDataGenerator {
    val savedGroupItem1 = SavedItem(
        id = 1,
        name = "1083/1",
        type = "GROUP",
        isSelected = true,
    )
    val savedGroupItem2 = SavedItem(
        id = 2,
        name = "1083/2",
        type = "GROUP",
        isSelected = false,
    )
    val professorSavedItem1 = SavedItem(
        id = 3,
        name = "Иван Иванович",
        type = "PROFESSOR",
        isSelected = true
    )
    val professorSavedItem2 = SavedItem(
        id = 4,
        name = "Леон Иванович",
        type = "PROFESSOR",
        isSelected = false
    )
}