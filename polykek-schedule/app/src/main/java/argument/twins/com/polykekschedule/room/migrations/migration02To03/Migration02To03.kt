package argument.twins.com.polykekschedule.room.migrations.migration02To03

import androidx.sqlite.db.SupportSQLiteDatabase
import argument.twins.com.polykekschedule.room.migrations.RoomMigration
import javax.inject.Inject

/**
 * Migration 02 to 03. Delete group-table (removed dao).
 *
 * @property migration02To03UseCase [Migration02To03UseCase]
 * @constructor Create [Migration02To03]
 */
class Migration02To03 @Inject constructor(private val migration02To03UseCase: Migration02To03UseCase) : RoomMigration() {
    override val startVersion: Int = 2

    override val endVersion: Int = 3

    override fun transactionMigration(database: SupportSQLiteDatabase) = migration02To03UseCase.transactionMigration(database)
}