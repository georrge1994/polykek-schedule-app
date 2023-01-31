package argument.twins.com.polykekschedule.room.migrations.dagger

import androidx.room.migration.Migration
import argument.twins.com.polykekschedule.room.migrations.migration02To03.Migration02To03
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
class RoomMigrationsModule {
    @Provides
    @IntoSet
    fun provideMigration01To02(migration01To02: Migration02To03): Migration = migration01To02.getMigration()

    @Provides
    @IntoSet
    fun provideMigration02To03(migration02To03: Migration02To03): Migration = migration02To03.getMigration()
}