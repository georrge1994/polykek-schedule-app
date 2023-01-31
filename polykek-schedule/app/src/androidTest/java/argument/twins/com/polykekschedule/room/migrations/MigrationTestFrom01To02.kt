package argument.twins.com.polykekschedule.room.migrations

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import argument.twins.com.polykekschedule.room.AppRoomDatabase
import argument.twins.com.polykekschedule.room.migrations.migration01To02.Migration01To02
import argument.twins.com.polykekschedule.room.migrations.migration01To02.Migration01To02UseCase
import argument.twins.com.polykekschedule.room.savedItems.*
import com.android.test.support.androidTest.base.BaseMigrationTest
import com.android.test.support.androidTest.base.TEST_DB
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

/**
 * Migration test from 01 to 02.
 *
 * @constructor Create empty constructor for migration test from 01 to 02
 */
class MigrationTestFrom01To02 : BaseMigrationTest<AppRoomDatabase>(AppRoomDatabase::class) {
    /**
     * Migrate 01 to 02.
     */
    @Test
    @Throws(IOException::class)
    fun migrate01To02() {
        val migration = Migration01To02(Migration01To02UseCase()).getMigration()
        // 1. Create database and insert data.
        helper.createDatabase(TEST_DB, 1).apply {
            this.insert(
                "saved_groups",
                SQLiteDatabase.CONFLICT_REPLACE,
                ContentValues(3).apply {
                    put("id", 1)
                    put("nameOneLine", "1083/1")
                    put("isSelectedGroup", 1)
                }
            )
            this.insert(
                "saved_groups",
                SQLiteDatabase.CONFLICT_REPLACE,
                ContentValues(3).apply {
                    put("id", 2)
                    put("nameOneLine", "1083/2")
                    put("isSelectedGroup", 0)
                }
            )
            this.close()
        }
        helper.runMigrationsAndValidate(TEST_DB, 2, true, migration).apply {
            val cursor = query("SELECT * FROM $SAVED_ITEMS")
            // Check first, selected group.
            cursor.moveToFirst()
            assertEquals(2, cursor.count)
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(SAVED_ITEMS_ID)))
            assertEquals("1083/1", cursor.getString(cursor.getColumnIndex(SAVED_ITEMS_NAME)))
            assertEquals("GROUP", cursor.getString(cursor.getColumnIndex(SAVED_ITEMS_TYPE)))
            assertEquals(1, cursor.getInt(cursor.getColumnIndex(SAVED_ITEMS_IS_SELECTED)))
            // Check second, non-selected group.
            cursor.moveToNext()
            assertEquals(2, cursor.getInt(cursor.getColumnIndex(SAVED_ITEMS_ID)))
            assertEquals("1083/2", cursor.getString(cursor.getColumnIndex(SAVED_ITEMS_NAME)))
            assertEquals("GROUP", cursor.getString(cursor.getColumnIndex(SAVED_ITEMS_TYPE)))
            assertEquals(0, cursor.getInt(cursor.getColumnIndex(SAVED_ITEMS_IS_SELECTED)))
            cursor.close()
        }
    }
}