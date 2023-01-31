package argument.twins.com.polykekschedule.dagger.core

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import argument.twins.com.polykekschedule.room.AppRoomDatabase
import argument.twins.com.polykekschedule.room.migrations.dagger.RoomMigrationsModule
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import javax.inject.Named
import javax.inject.Singleton

internal const val DATABASE_DISPATCHER = "DatabaseDispatcher"
internal const val DATABASE_NAME = "polytech_database"

@Module(
    includes = [
        CoreRoomRepositoryBinder::class,
        RoomMigrationsModule::class
    ]
)
class CoreRoomModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(
        application: Application,
        roomMigrations: Set<@JvmSuppressWildcards Migration>
    ) = Room.databaseBuilder(
        application,
        AppRoomDatabase::class.java,
        DATABASE_NAME
    ).addMigrations(*roomMigrations.toTypedArray())
        .fallbackToDestructiveMigrationOnDowngrade()
        .build()

    @Provides
    @Singleton
    @Named(DATABASE_DISPATCHER)
    fun provideDatabaseDispatcher(): CoroutineDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Provides
    @Singleton
    fun provideNotesDao(appRoomDatabase: AppRoomDatabase) = appRoomDatabase.notesDao()

    @Provides
    @Singleton
    fun provideSavedItemsDao(appRoomDatabase: AppRoomDatabase) = appRoomDatabase.savedItemsDao()
}