package argument.twins.com.polykekschedule.dagger.core

import argument.twins.com.polykekschedule.room.notes.NotesRoomRepository
import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.retrofit.impl.dagger.ICoreRetrofitDependencies
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.core.ui.dagger.ICoreUiModuleDependencies
import com.android.module.injector.dependenciesHolders.*
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.android.schedule.controller.api.*
import com.android.schedule.controller.impl.dagger.IScheduleControllerModuleDependencies
import com.android.schedule.controller.impl.dagger.ScheduleControllerComponentHolder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

internal const val SCHEDULE_CONTROLLER_DYNAMIC_DEPENDENCIES_PROVIDER = "SCHEDULE_CONTROLLER_DYNAMIC_DEPENDENCIES_PROVIDER"

/**
 * Core retrofit module.
 *
 * @constructor Create empty constructor for core retrofit module
 */
@Module
class ScheduleControllerModule {
    private class ScheduleControllerDependencyHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        coreRetrofitModule: ICoreRetrofitModuleApi,
        override val block: (IBaseDependencyHolder<IScheduleControllerModuleDependencies>, ICoreUiModuleApi, ICoreRetrofitModuleApi) -> IScheduleControllerModuleDependencies
    ) : DependencyHolder2<ICoreUiModuleApi, ICoreRetrofitModuleApi, IScheduleControllerModuleDependencies>(coreUiModuleApi, coreRetrofitModule)

    @Provides
    @Singleton
    @Named(SCHEDULE_CONTROLLER_DYNAMIC_DEPENDENCIES_PROVIDER)
    fun provideScheduleControllerDynamicDependenciesProvider(
        @Named(CORE_UI_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreUi: DynamicProvider<ICoreUiModuleDependencies>,
        @Named(CORE_RETROFIT_DYNAMIC_DEPENDENCIES_PROVIDER) dynamicDependencyProviderForCoreRetrofit: DynamicProvider<ICoreRetrofitDependencies>,
        savedItemsRoomRepository: ISavedItemsRoomRepository,
        notesRoomRepository: NotesRoomRepository
    ): DynamicProvider<IScheduleControllerModuleDependencies> = DynamicProvider {
        ScheduleControllerDependencyHolder(
            CoreUiComponentHolder.initAndGet(dynamicDependencyProviderForCoreUi),
            CoreRetrofitComponentHolder.initAndGet(dynamicDependencyProviderForCoreRetrofit)
        ) { dependencyHolder, coreUiModuleApi, coreRetrofitModuleApi ->
            object : IScheduleControllerModuleDependencies {
                override val retrofit: Retrofit
                    get() = coreRetrofitModuleApi.retrofit
                override val notesRoomRepository: INotesRoomRepository
                    get() = notesRoomRepository
                override val savedItemsRoomRepository: ISavedItemsRoomRepository
                    get() = savedItemsRoomRepository
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}