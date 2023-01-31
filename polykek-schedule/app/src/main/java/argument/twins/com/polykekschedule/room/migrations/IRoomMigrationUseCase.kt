package argument.twins.com.polykekschedule.room.migrations

import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Room migration use case. Every each migration made as useCase.
 * It helps to reuse them in user migration - all dev-migrations like one prod-migration.
 */
interface IRoomMigrationUseCase {
    /**
     * Execute sql package.
     *
     * @param database Database
     */
    fun transactionMigration(database: SupportSQLiteDatabase)
}