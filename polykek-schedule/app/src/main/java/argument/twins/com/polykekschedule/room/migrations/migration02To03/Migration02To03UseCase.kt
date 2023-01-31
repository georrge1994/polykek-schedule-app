package argument.twins.com.polykekschedule.room.migrations.migration02To03

import androidx.sqlite.db.SupportSQLiteDatabase
import argument.twins.com.polykekschedule.room.migrations.IRoomMigrationUseCase
import javax.inject.Inject

/**
 * Migration 02 to 03 use case. Delete group-table (removed dao).
 *
 * @constructor Create empty constructor for migration 02 to 03 use case
 */
class Migration02To03UseCase @Inject constructor() : IRoomMigrationUseCase {
    override fun transactionMigration(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS 'saved_groups'")
    }
}