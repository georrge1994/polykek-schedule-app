package argument.twins.com.polykekschedule.room.migrations

import argument.twins.com.polykekschedule.room.AppRoomDatabase
import argument.twins.com.polykekschedule.room.migrations.migration02To03.Migration02To03
import argument.twins.com.polykekschedule.room.migrations.migration02To03.Migration02To03UseCase
import com.android.test.support.androidTest.base.BaseMigrationTest
import com.android.test.support.androidTest.base.TEST_DB
import org.junit.Test
import java.io.IOException

/**
 * Migration test from 02 to 03.
 *
 * @constructor Create empty constructor for migration test from 02 to 03
 */
class MigrationTestFrom02To03 : BaseMigrationTest<AppRoomDatabase>(AppRoomDatabase::class) {
    /**
     * Migrate 02 to 03.
     */
    @Test
    @Throws(IOException::class)
    fun migrate02To03() {
        val migration = Migration02To03(Migration02To03UseCase()).getMigration()
        helper.createDatabase(TEST_DB, 2)
        helper.runMigrationsAndValidate(TEST_DB, 3, true, migration)
    }
}