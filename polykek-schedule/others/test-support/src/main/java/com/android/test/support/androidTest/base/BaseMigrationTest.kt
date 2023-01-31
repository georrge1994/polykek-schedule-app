package com.android.test.support.androidTest.base

import android.database.Cursor
import androidx.room.RoomDatabase
import androidx.room.testing.MigrationTestHelper
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import kotlin.reflect.KClass

const val TEST_DB = "migration-test"

/**
 * Base migration test provides general methods for migration unit tests.
 *
 * @constructor Create empty Base migration test
 */
abstract class BaseMigrationTest<T : RoomDatabase>(clazz: KClass<T>) {
    @Rule
    @JvmField
    val helper: MigrationTestHelper = MigrationTestHelper(InstrumentationRegistry.getInstrumentation(), clazz.java)

    /**
     * Get content. Short-cut function.
     *
     * @param key Column key
     * @return Content from table item
     */
    protected fun Cursor.getContent(key: String): String? {
        val index = getColumnIndex(key)
        return if (index == -1)
            null
        else
            getString(index)
    }
}