package argument.twins.com.polykekschedule.room.migrations.migration01To02

import androidx.sqlite.db.SupportSQLiteDatabase
import argument.twins.com.polykekschedule.room.migrations.RoomMigration
import javax.inject.Inject

/**
 * Migration 01 to 02. Migrate from group-table to selected-item-table.
 *
 * @property migration01To02UseCase [Migration01To02UseCase]
 * @constructor Create [Migration01To02]
 */
class Migration01To02 @Inject constructor(private val migration01To02UseCase: Migration01To02UseCase) : RoomMigration() {
    override val startVersion: Int = 1

    override val endVersion: Int = 2

    override fun transactionMigration(database: SupportSQLiteDatabase) = migration01To02UseCase.transactionMigration(database)
}