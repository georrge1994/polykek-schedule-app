package argument.twins.com.polykekschedule.dagger.core

import argument.twins.com.polykekschedule.dagger.collector.DynamicDependenciesProviderFactory
import com.android.core.retrofit.api.ICoreRetrofitModuleApi
import com.android.core.retrofit.impl.dagger.CoreRetrofitComponentHolder
import com.android.core.room.api.notes.INotesRoomRepository
import com.android.core.room.api.savedItems.ISavedItemsRoomRepository
import com.android.core.ui.dagger.CoreUiComponentHolder
import com.android.core.ui.dagger.ICoreUiDependencies
import com.android.core.ui.dagger.ICoreUiModuleApi
import com.android.module.injector.dependenciesHolders.DependencyHolder2
import com.android.module.injector.dependenciesHolders.DynamicProvider
import com.android.module.injector.dependenciesHolders.IBaseDependencyHolder
import com.android.module.injector.moduleMarkers.IModuleDependencies
import com.android.schedule.controller.impl.dagger.IScheduleControllerModuleDependencies
import com.android.schedule.controller.impl.dagger.ScheduleControllerComponentHolder
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Schedule controller dynamic provider factory. Injected class have to be part of app-core.
 *
 * @property notesRoomRepository Notes room repository to work with notes in the memory
 * @property savedItemsRoomRepository Stores selected groups and professors (selected items)
 * @constructor Create [ScheduleControllerDynamicProviderFactory]
 */
class ScheduleControllerDynamicProviderFactory @Inject constructor(
    private val notesRoomRepository: INotesRoomRepository,
    private val savedItemsRoomRepository: ISavedItemsRoomRepository
) : DynamicDependenciesProviderFactory<ScheduleControllerComponentHolder, IScheduleControllerModuleDependencies>(
    ScheduleControllerComponentHolder
) {
    private class ScheduleControllerDependencyHolder(
        coreUiModuleApi: ICoreUiModuleApi,
        coreRetrofitModule: ICoreRetrofitModuleApi,
        override val block: (IBaseDependencyHolder<IScheduleControllerModuleDependencies>, ICoreUiModuleApi, ICoreRetrofitModuleApi) -> IScheduleControllerModuleDependencies
    ) : DependencyHolder2<ICoreUiModuleApi, ICoreRetrofitModuleApi, IScheduleControllerModuleDependencies>(
        coreUiModuleApi,
        coreRetrofitModule
    )

    override fun getDynamicProvider(): DynamicProvider<IScheduleControllerModuleDependencies> = DynamicProvider {
        ScheduleControllerDependencyHolder(
            CoreUiComponentHolder.getApi(),
            CoreRetrofitComponentHolder.getApi()
        ) { dependencyHolder, coreUiModuleApi, coreRetrofitModuleApi ->
            object : IScheduleControllerModuleDependencies {
                override val retrofit: Retrofit
                    get() = coreRetrofitModuleApi.retrofit
                override val notesRoomRepository: INotesRoomRepository
                    get() = this@ScheduleControllerDynamicProviderFactory.notesRoomRepository
                override val savedItemsRoomRepository: ISavedItemsRoomRepository
                    get() = this@ScheduleControllerDynamicProviderFactory.savedItemsRoomRepository
                override val coreBaseUiDependencies: ICoreUiDependencies
                    get() = coreUiModuleApi.coreUiDependencies
                override val dependencyHolder: IBaseDependencyHolder<out IModuleDependencies>
                    get() = dependencyHolder
            }
        }.dependencies
    }
}