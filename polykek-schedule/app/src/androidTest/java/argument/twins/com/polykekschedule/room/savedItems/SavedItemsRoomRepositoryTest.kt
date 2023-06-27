package argument.twins.com.polykekschedule.room.savedItems

import argument.twins.com.polykekschedule.room.AppRoomDatabase
import argument.twins.com.polykekschedule.room.dataGenerator.SavedItemDataGenerator
import com.android.shared.code.utils.general.SharedPreferenceUtils
import com.android.test.support.androidTest.base.BaseRoomRepositoryTest
import com.android.test.support.androidTest.utils.getOrAwaitValueScoped
import com.android.test.support.testFixtures.runBlockingUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Saved items room repository test for [SavedItemsRoomRepository].
 *
 * @constructor Create empty constructor for saved items room repository test
 */
class SavedItemsRoomRepositoryTest : BaseRoomRepositoryTest<AppRoomDatabase>(AppRoomDatabase::class) {
    private val savedItemDataGenerator = SavedItemDataGenerator()
    private lateinit var savedItemsRoomRepository: SavedItemsRoomRepository
    private val sharedPreferenceUtils: SharedPreferenceUtils = mockk {
        coEvery { contains(any()) } returns true
        coEvery { add(any(), any()) } returns Unit
        coEvery { remove(any()) } returns Unit
    }

    override fun beforeTest() {
        super.beforeTest()
        savedItemsRoomRepository = SavedItemsRoomRepository(
            savedItemsDao = db.savedItemsDao(),
            sharedPreferenceUtils = sharedPreferenceUtils,
            databaseDispatcher = unconfinedTestDispatcher
        )
    }

    @Test
    fun isItemSelected() {
        assertTrue(savedItemsRoomRepository.isItemSelected)
        coVerify(exactly = 1) { sharedPreferenceUtils.contains("GROUP_IS_SELECTED") }
    }

    /**
     * Saved items.
     */
    @Test
    fun savedItems() = runBlockingUnit {
        insertTestDataToDb()
        savedItemsRoomRepository.savedItems.getOrAwaitValue(
            listOf(
                savedItemDataGenerator.savedGroupItem1,
                savedItemDataGenerator.savedGroupItem2,
                savedItemDataGenerator.professorSavedItem2
            )
        )
    }

    /**
     * Save item and select it.
     */
    @Test
    fun saveItemAndSelectIt() = runBlockingUnit {
        savedItemsRoomRepository.saveItemAndSelectIt(savedItemDataGenerator.professorSavedItem1)
        coVerify(exactly = 1) { sharedPreferenceUtils.add("GROUP_IS_SELECTED", true) }
    }

    /**
     * Select item.
     */
    @Test
    fun selectItem() = runBlockingUnit {
        insertTestDataToDb()
        savedItemsRoomRepository.selectItem(savedItemDataGenerator.savedGroupItem2)
        savedItemsRoomRepository.getSelectedItemFlow().subscribeAndCompareFirstValue(savedItemDataGenerator.savedGroupItem2)
    }

    /**
     * Delete.
     */
    @Test
    fun delete() = runBlockingUnit {
        insertTestDataToDb()
        savedItemsRoomRepository.delete(savedItemDataGenerator.savedGroupItem1)
        savedItemsRoomRepository.savedItems.getOrAwaitValue(
            listOf(
                savedItemDataGenerator.savedGroupItem2,
                savedItemDataGenerator.professorSavedItem2
            )
        )
        coVerify(exactly = 1) { sharedPreferenceUtils.remove("GROUP_IS_SELECTED") }
    }

    /**
     * Insert test data to db.
     */
    private suspend fun insertTestDataToDb() {
        savedItemsRoomRepository.insert(savedItemDataGenerator.savedGroupItem1)
        savedItemsRoomRepository.insert(savedItemDataGenerator.savedGroupItem2)
        savedItemsRoomRepository.insert(savedItemDataGenerator.professorSavedItem2)
    }
}