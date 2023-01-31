package argument.twins.com.polykekschedule.room.migrations.migration01To02

import androidx.sqlite.db.SupportSQLiteDatabase
import argument.twins.com.polykekschedule.room.migrations.IRoomMigrationUseCase
import argument.twins.com.polykekschedule.room.savedItems.SAVED_ITEMS
import javax.inject.Inject

/**
 * Migration 01 to 02 use case. Migrate from group-table to selected-item-table
 *
 * @constructor Create empty constructor for migration 01 to 02 use case
 */
class Migration01To02UseCase @Inject constructor() : IRoomMigrationUseCase {
    override fun transactionMigration(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS $SAVED_ITEMS (" +
                    "id INTEGER NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "type TEXT NOT NULL DEFAULT 'GROUP', " +
                    "isSelected INTEGER NOT NULL, " +
                    "PRIMARY KEY(id)" +
                    ")"
        )
        database.execSQL(
            "INSERT INTO $SAVED_ITEMS (id, name, isSelected) " +
                    "SELECT id, nameOneLine, isSelectedGroup FROM 'saved_groups'"
        )
    }
}