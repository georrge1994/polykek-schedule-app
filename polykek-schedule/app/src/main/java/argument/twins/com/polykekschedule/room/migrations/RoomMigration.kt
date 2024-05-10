package argument.twins.com.polykekschedule.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Room migration.
 *
 * @constructor Create [RoomMigration]
 */
abstract class RoomMigration : IRoomMigrationUseCase {
    protected abstract val startVersion: Int

    protected abstract val endVersion: Int

    /**
     * Get migration for some specific version. This wrapper will protect migration exception, because every each
     * migration will be called like a transaction.
     *
     * @return [Migration]
     */
    fun getMigration(): Migration = object : Migration(startVersion, endVersion) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.beginTransactionNonExclusive()
            transactionMigration(db)
            db.setTransactionSuccessful()
            db.endTransaction()
        }
    }
}